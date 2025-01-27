package com.tools.payhelper.pay.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.payhelper.R
import com.tools.payhelper.pay.PayHelperUtils

class NewsListActivity : AppCompatActivity() {
    var adapter: Adapter? = null
    var newsListData: ArrayList<NewsListData.Data> = ArrayList()
    val newsListViewModel: NewsListViewModel by lazy {
        ViewModelProvider(this, NewsListViewModelFactory()).get(NewsListViewModel::class.java)
    }

    lateinit var recyclerView: RecyclerView
    lateinit var closebtn : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newslist)
        title = "公告栏"
        getListItem()
        adapter = Adapter()
        recyclerView = findViewById(R.id.recycler_view)
        closebtn = findViewById(R.id.closeBtn)
        closebtn.setOnClickListener {
            finish()
        }
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter!!.updateList(newsListData)

        recyclerView!!.adapter = adapter

        adapter!!.notifyDataSetChanged()

    }

    fun getListItem(){

        newsListViewModel.getNewsList(this).observe(this, Observer {
            newsListData.clear()
            if (it!=null){
                if(it.code==0){
                    if (it.data!=null){

                        for (datum in it.data) {
                            newsListData.add(datum)
                            adapter!!.notifyDataSetChanged()

                        }
                    }
                }

            }
        })
    }

    class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {
        var bankCardInfoList: ArrayList<NewsListData.Data>? = null


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var cardNo: TextView
            var time: TextView
            var orderno: TextView


            init {
                cardNo = view.findViewById(R.id.cardno)
                time = view.findViewById(R.id.time)
                orderno = view.findViewById(R.id.orderno)


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info: NewsListData.Data = bankCardInfoList!!.get(position)

            var isTopString = if (info.isTop) "置顶公告" else "一般公告"
            holder.orderno.text = isTopString
            holder.cardNo.text = info.istext
            var timeS = info.SapplyDate.substring(0, info.SapplyDate.indexOf("T"));
            holder.time.text = timeS



        }

        override fun getItemCount(): Int {
            return bankCardInfoList!!.size
        }

        //更新資料用
        fun updateList(list: ArrayList<NewsListData.Data>){
            bankCardInfoList = list
        }


    }
}