package com.wx.app.wxapp.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wx.app.wxapp.R
import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.mvp.constract.MediaListContract
import com.wx.app.wxapp.mvp.module.MediaListModule
import com.wx.app.wxapp.mvp.presenter.MediaListPresenter
import com.wx.app.wxapp.ui.adapter.HomeListAdapter
import com.wx.app.wxapp.ui.fragment.base.BaseContentFragment
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_home_list.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/7/007

 */
class HomeListFragment : BaseContentFragment<MediaListContract.MediaListView, MediaListContract.MediaListModule, MediaListPresenter>(), MediaListContract.MediaListView {


    private var nextPageUrl: String = ""

    val list = ArrayList<MediaBean.Issue.Item.Data>()

    companion object {
        private var type: Int = 0
        private const val keys: String = "key"
        fun newInstance(type: Int): HomeListFragment {
            val homeListFragment = HomeListFragment()
            val bundle = Bundle()
            bundle.putInt(keys, type)
            homeListFragment.arguments = bundle
            return homeListFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        type = bundle!!.getInt(keys)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun chileView(): MediaListContract.MediaListView = this

    override fun chileModule(): MediaListContract.MediaListModule = MediaListModule()

    override fun createPresenter(): MediaListPresenter = MediaListPresenter()

    override fun layoutId(): Int = R.layout.fragment_home_list

    override fun statusViewId(): MultipleStatusView = vw_multiple

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun complete() {
        multipleStatusView.showContent()
    }

    override fun loadMoreLoading() {
    }

    override fun loadMoreComplete() {
    }


    override fun showList() {
        mPresenter.loadData(type)
    }


    override fun initView() {
    }

    override fun initData() {
        showList()
//        when (type) {
//            0 -> {
//                View.inflate(context,)
//            }
//            else -> {
//                showList()
//            }
//
//        }
    }

    override fun showError(msg: String, errorCode: Int) {
        //TODO:展示列表获取错误
    }


    override fun showNextLoadDataError(esg: String, errorType: Int) {

    }

    override fun loadListData(mediaBean: MediaBean) {
        var itemList = mediaBean.issueList.let {
            it[0].itemList
        }
        val adapters = HomeListAdapter(R.layout.item_home_content, itemList)
        rc_list.adapter = adapters
        rc_list.layoutManager = LinearLayoutManager(context)

//        adapters.setLoadMoreView(SimpleLoadMoreView())
//
//        adapters.setOnLoadMoreListener({
//            nextPageUrl = mediaBean.nextPageUrl
//            mPresenter.loadNextData(nextPageUrl)
//        },rc_list)


    }

    override fun loadNextSuccess(mediaBean: MediaBean) {
    }

}