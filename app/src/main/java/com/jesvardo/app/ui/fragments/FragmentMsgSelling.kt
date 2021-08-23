package com.jesvardo.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesvardo.app.R
import com.jesvardo.app.ui.ActivityAddVehicleDetails
import com.jesvardo.app.ui.ActivityChat
import com.jesvardo.app.utils.listeners.setSafeOnClickListener

class FragmentMsgSelling : Fragment() {

    private lateinit var createdView: View

    private var intPosition : Int = 0

    lateinit var baseActivity: ActivityChat

    lateinit var filterType: ArrayList<String>

    private lateinit var filterAdapter: FilterAdapter
    private lateinit var messageListAdapter: MessageListAdapter

    lateinit var recyclerViewType: RecyclerView
    lateinit var recyclerViewMsg : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intPosition = arguments!!.getInt("position")
        baseActivity = activity as ActivityChat

    }

    companion object {
        fun newInstance(position: Int): Fragment {
            val fragment = FragmentMsgAll()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createdView = inflater.inflate(R.layout.fragment_msg, container, false)

        recyclerViewType = createdView.findViewById<RecyclerView>(R.id.fragment_msg_recycler_type)
        recyclerViewMsg = createdView.findViewById<RecyclerView>(R.id.fragment_msg_recycler_msg_list)

        filterType = ArrayList<String>()
        filterType.add("All")
        filterType.add("Read")
        filterType.add("Unread")
        filterType.add("Important")

        var mLayoutManager = LinearLayoutManager(baseActivity)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        filterAdapter = FilterAdapter(baseActivity, filterType)
        recyclerViewType.layoutManager = mLayoutManager
        recyclerViewType.adapter = filterAdapter


        mLayoutManager = LinearLayoutManager(baseActivity)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        messageListAdapter = MessageListAdapter()
        recyclerViewMsg.layoutManager = mLayoutManager
        recyclerViewMsg.adapter = messageListAdapter


        return createdView
    }

    class FilterAdapter(var baseActivity: ActivityChat, val filterType: ArrayList<String>) : RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

        var selectedPotion: Int = 0

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView = view.findViewById<TextView>(R.id.raw_dash_filter_list_txt_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context) .inflate(R.layout.raw_dash_filter_list, parent, false)
            return FilterAdapter.MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return filterType.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemView.setSafeOnClickListener {
                selectedPotion = position
                notifyDataSetChanged()
            }

            holder.textViewName.text = filterType[position]

            if (position == selectedPotion) {
                holder.textViewName.setBackgroundResource(R.drawable.bg_msg_list_selected)
                holder.textViewName.setTextColor(ContextCompat.getColor(baseActivity, R.color.white))
            } else {
                holder.textViewName.setBackgroundResource(R.drawable.bg_msg_list_unselected)
                holder.textViewName.setTextColor(ContextCompat.getColor(baseActivity, R.color.color_still_grey))
            }
        }
    }

    class MessageListAdapter() : RecyclerView.Adapter<MessageListAdapter.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context) .inflate(R.layout.raw_message_list, parent, false)
            return MessageListAdapter.MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        }
    }
}