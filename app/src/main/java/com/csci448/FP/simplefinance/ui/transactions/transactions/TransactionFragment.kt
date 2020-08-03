package com.csci448.FP.simplefinance.ui.transactions.transactions

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*

import android.widget.SearchView
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci448.FP.simplefinance.R
import com.csci448.FP.simplefinance.data.transactions_data.Transactions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.math.log

private const val logTag="448.TransactionFragment"
private const val ARG_TRANSACTION_ID = "transaction_id"
private lateinit var  transactionFragmentViewModel: TransactionFragmentViewModel

class TransactionFragment : Fragment() {


    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var transactionSearchView: SearchView
    private lateinit var fab: FloatingActionButton
    private var mediaPlayer: MediaPlayer? = null

    private fun updateUI(transactions: List<Transactions>, query: String) {
        val filteredTransactions = transactionFragmentViewModel.applyTransactionFilter(transactions, query)
        adapter = TransactionAdapter(filteredTransactions) { transaction: Transactions -> Unit
            val action = TransactionFragmentDirections.actionTransPageToTransactionsDetailFragment(transaction.id)
            findNavController().navigate(action)
        }
        transactionsRecyclerView.adapter = adapter
    }

    fun clearList() {
        updateUI(emptyList(), "")
    }

    fun restartList(query: String?) {

        var newQuery = ""
        if(query == null) {
            newQuery = ""
        } else {
            newQuery = query
        }
        transactionFragmentViewModel.transactionListLiveData.observe(viewLifecycleOwner, Observer { transactions ->
            transactions?.let {
                Log.i(logTag, "Got transactions ${transactions.size}")
                updateUI(transactions, newQuery)
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "on attach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().title = "My Transactions"
        Log.d(logTag, "on create called")
        val factory = TransactionFragmentViewModelFactory(requireContext())
        transactionFragmentViewModel = ViewModelProvider(this, factory).get(TransactionFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.view_transactions_list, container, false)

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            val action = TransactionFragmentDirections.actionTransPageToTransactionsFormFragment()
            findNavController().navigate(action)
            mediaPlayer = MediaPlayer.create(requireContext(),R.raw.click)
            mediaPlayer?.setOnPreparedListener{
                Log.d(logTag, "player prepared")
            }
            mediaPlayer?.start()
        }

        transactionsRecyclerView= view.findViewById(R.id.transaction_list_recycler_view) as RecyclerView
        transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
        transactionSearchView = view.findViewById(R.id.trans_search_view)
        clearList()
        Log.d(logTag, "on create view called")
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(logTag, "on view created called")

        restartList("")
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        val queryTransactionListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                clearList()
                restartList(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        }

        transactionSearchView.setOnQueryTextListener(queryTransactionListener)
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")

    }



    companion object{
        fun newInstance(transactionId: UUID):TransactionFragment{
            val args = Bundle().apply{
                putSerializable(ARG_TRANSACTION_ID, transactionId)
            }
            return TransactionFragment().apply{
                arguments = args
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(logTag, "on detatch called")
    }
}