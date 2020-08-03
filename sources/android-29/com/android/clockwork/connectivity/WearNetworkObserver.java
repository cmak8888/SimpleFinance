package com.android.clockwork.connectivity;

import android.content.Context;
import android.net.NetworkCapabilities;
import android.net.NetworkFactory;
import android.net.NetworkRequest;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.util.IndentingPrintWriter;
import com.android.clockwork.common.DebugAssert;

import java.util.Locale;

/**
 * A dummy NetworkFactory whose primary job is to monitor NetworkRequests as they come in
 * and out of ConnectivityService, so that it may keep track of the count of requests based
 * on different characteristics (i.e. unmetered network requests) and bounce that information
 * to a registered listener.
 */
public class WearNetworkObserver extends NetworkFactory {

    private static final String TAG = "WearNetworkObserver";
    private static final String NETWORK_TYPE = "WEAR_NETWORK_OBSERVER";

    // At its best performance, Bluetooth proxy provides 200kbps of bandwidth, so any request
    // for a bandwidth higher than this is considered "high bandwidth".
    private static final int HIGH_BANDWIDTH_KBPS = 200 * 1024;

    public interface Listener {
        void onUnmeteredRequestsChanged(int numUnmeteredRequests);
        void onHighBandwidthRequestsChanged(int numHighBandwidthRequests);
        void onWifiRequestsChanged(int numWifiRequests);
        void onCellularRequestsChanged(int numCellularRequests);
    }

    private final SparseArray<NetworkRequest> mUnmeteredRequests = new SparseArray<>();
    private final SparseArray<NetworkRequest> mHighBandwidthRequests = new SparseArray<>();
    private final SparseArray<NetworkRequest> mWifiRequests = new SparseArray<>();
    private final SparseArray<NetworkRequest> mCellularRequests = new SparseArray<>();
    private final Listener mListener;

    public WearNetworkObserver(final Context context, Listener listener) {
        super(Looper.getMainLooper(), context, NETWORK_TYPE, null);
        DebugAssert.isMainThread();
        mListener = listener;
    }

    @Override
    protected void handleAddRequest(NetworkRequest req, int score) {
        DebugAssert.isMainThread();
        verboseLog("WearNetworkObserver: handleAddRequest");

        if (mUnmeteredRequests.get(req.requestId) == null
                && req.networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_NOT_METERED)) {
            mUnmeteredRequests.put(req.requestId, req);
            mListener.onUnmeteredRequestsChanged(mUnmeteredRequests.size());
        }

        if (mHighBandwidthRequests.get(req.requestId) == null
                && (req.networkCapabilities.getLinkDownstreamBandwidthKbps()
                        > HIGH_BANDWIDTH_KBPS)) {
            mHighBandwidthRequests.put(req.requestId, req);
            mListener.onHighBandwidthRequestsChanged(mHighBandwidthRequests.size());
        }

        if (mWifiRequests.get(req.requestId) == null
                && req.networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            mWifiRequests.put(req.requestId, req);
            mListener.onWifiRequestsChanged(mWifiRequests.size());
        }

        if (mCellularRequests.get(req.requestId) == null
                && req.networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            mCellularRequests.put(req.requestId, req);
            mListener.onCellularRequestsChanged(mCellularRequests.size());
        }

        verboseLog(String.format(Locale.US,
                "handleAddRequest - [unmetered %d // highband %d // wifi %d // cell %d ]",
                mUnmeteredRequests.size(),
                mHighBandwidthRequests.size(),
                mWifiRequests.size(),
                mCellularRequests.size()));
    }

    @Override
    public void handleRemoveRequest(NetworkRequest req) {
        DebugAssert.isMainThread();
        verboseLog("WearNetworkObserver: handleRemoveRequest");
        NetworkRequest unmeteredReq = mUnmeteredRequests.get(req.requestId);
        if (unmeteredReq != null) {
            mUnmeteredRequests.remove(req.requestId);
            mListener.onUnmeteredRequestsChanged(mUnmeteredRequests.size());
        }

        NetworkRequest highBandwidthReq = mHighBandwidthRequests.get(req.requestId);
        if (highBandwidthReq != null) {
            mHighBandwidthRequests.remove(req.requestId);
            mListener.onHighBandwidthRequestsChanged(mHighBandwidthRequests.size());
        }

        NetworkRequest wifiReq = mWifiRequests.get(req.requestId);
        if (wifiReq != null) {
            mWifiRequests.remove(req.requestId);
            mListener.onWifiRequestsChanged(mWifiRequests.size());
        }

        NetworkRequest cellReq = mCellularRequests.get(req.requestId);
        if (cellReq != null) {
            mCellularRequests.remove(req.requestId);
            mListener.onCellularRequestsChanged(mCellularRequests.size());
        }

        verboseLog(String.format(Locale.US,
                "handleRemoveRequest - [unmetered %d // highband %d // wifi %d // cell %d ]",
                mUnmeteredRequests.size(),
                mHighBandwidthRequests.size(),
                mWifiRequests.size(),
                mCellularRequests.size()));
    }

    private void verboseLog(String msg) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, msg);
        }
    }

    /**
     * This method may dump memory-inconsistent values when called off the main thread.
     * This is preferable to synchronizing every call to handleAddRequest/handleRemoveRequest.
     */
    public void dump(IndentingPrintWriter ipw) {
        ipw.println("======== WearNetworkObserver ========");
        ipw.printPair("Outstanding unmetered network requests", mUnmeteredRequests.size());
        ipw.println();
        ipw.printPair("Outstanding high-bandwidth network requests", mHighBandwidthRequests.size());
        ipw.println();
        ipw.printPair("Outstanding wifi network requests", mWifiRequests.size());
        ipw.println();
        ipw.printPair("Outstanding cellular network requests", mCellularRequests.size());
        ipw.println();

        ipw.println("Unmetered requests: ");
        ipw.increaseIndent();
        for (int i = 0; i < mUnmeteredRequests.size(); i++) {
            NetworkRequest req = mUnmeteredRequests.valueAt(i);
            // extra null-guard in case the object was removed while we were iterating
            if (req != null) {
                ipw.print(req.toString());
            }
        }
        ipw.decreaseIndent();

        ipw.println();
        ipw.println("High-bandwidth requests: ");
        ipw.increaseIndent();
        for (int i = 0; i < mHighBandwidthRequests.size(); i++) {
            // extra null-guard in case the object was removed while we were iterating
            NetworkRequest req = mHighBandwidthRequests.valueAt(i);
            if (req != null) {
                ipw.print(req.toString());
            }
        }
        ipw.decreaseIndent();

        ipw.println();
        ipw.println("Wifi requests: ");
        ipw.increaseIndent();
        for (int i = 0; i < mWifiRequests.size(); i++) {
            // extra null-guard in case the object was removed while we were iterating
            NetworkRequest req = mWifiRequests.valueAt(i);
            if (req != null) {
                ipw.print(req.toString());
            }
        }
        ipw.decreaseIndent();

        ipw.println();
        ipw.println("Cellular requests: ");
        ipw.increaseIndent();
        for (int i = 0; i < mCellularRequests.size(); i++) {
            // extra null-guard in case the object was removed while we were iterating
            NetworkRequest req = mCellularRequests.valueAt(i);
            if (req != null) {
                ipw.print(req.toString());
            }
        }
        ipw.decreaseIndent();
    }
}
