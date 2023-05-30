package com.example.wanandroidmvp.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.media.metrics.Event
import android.os.Build
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.TodoPopupAdapter
import com.example.wanandroidmvp.base.BaseSwipeBackActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.event.TodoEvent
import com.example.wanandroidmvp.event.TodoTypeEvent
import com.example.wanandroidmvp.mvp.model.bean.TodoTypeBean
import com.example.wanandroidmvp.ui.fragment.TodoFragment
import com.example.wanandroidmvp.utils.DisplayManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout.LabelVisibility
import kotlinx.android.synthetic.main.activity_score.*
import kotlinx.android.synthetic.main.activity_todo.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TodoActivity : BaseSwipeBackActivity() {

    private var mType = 0

    private var mTodoFragment: TodoFragment? = null

    private lateinit var datas: MutableList<TodoTypeBean>

    private var mSwitchPopupWindow: PopupWindow? = null

    override fun attachLayoutRes(): Int = R.layout.activity_todo

    override fun initData() {
        datas = getTypeData()
    }

    override fun initView() {
        toolbar.run {
            title = datas[0].name
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        bottom_navigation.run {
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener(onNavigationItemSelectedListener)
        }

        floating_action_btn.run {
            setOnClickListener {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_ADD, mType))
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (mTodoFragment == null) {
            mTodoFragment = TodoFragment.getInstance(mType)
            transaction.add(R.id.container, mTodoFragment!!, "todo")
        } else {
            transaction.show(mTodoFragment!!)
        }
        transaction.commit()
    }

    override fun start() {
    }

    private fun getTypeData(): MutableList<TodoTypeBean> {
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0, "只用这一个", true))
        list.add(TodoTypeBean(1, "工作", false))
        list.add(TodoTypeBean(2, "学习", false))
        list.add(TodoTypeBean(3, "生活", false))
        return list
    }

    private fun initPopupWindow(dataList: List<TodoTypeBean>) {
        val recyclerView =
            layoutInflater.inflate(R.layout.layout_popup_todo, null) as RecyclerView
        val adapter = TodoPopupAdapter()
        adapter.setList(dataList)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            mSwitchPopupWindow?.dismiss()
            val itemData = adapter.data[position] as TodoTypeBean
            mType = itemData.type
            toolbar.title = itemData.name
            adapter.data.forEachIndexed { index, any ->
                val item = any as TodoTypeBean
                item.isSelected = index == position
            }
            adapter.notifyDataSetChanged()
            bottom_navigation.selectedItemId = R.id.action_notodo
            EventBus.getDefault().post(TodoTypeEvent(mType))
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodoActivity)
            this.adapter = adapter
        }
        mSwitchPopupWindow = PopupWindow(recyclerView)
        mSwitchPopupWindow?.apply {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = DisplayManager.dip2px(10F).toFloat()
            }
            setOnDismissListener {
                dismiss()
            }
            setTouchInterceptor { v, event ->
                if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    dismiss()
                    true
                }
                false
            }
        }
    }

    private fun showPopupWindow(dataList: MutableList<TodoTypeBean>) {
        if (mSwitchPopupWindow == null) initPopupWindow(dataList)
        if (mSwitchPopupWindow?.isShowing == true) mSwitchPopupWindow?.dismiss()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSwitchPopupWindow?.showAsDropDown(toolbar, -DisplayManager.dip2px(5F), 0, Gravity.END)
        } else {
            mSwitchPopupWindow?.showAsDropDown(
                toolbar,
                Gravity.BOTTOM,
                -DisplayManager.dip2px(5F),
                0
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_todo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_todo_type -> {
                showPopupWindow(datas)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    private val onNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            return@OnItemSelectedListener when (item.itemId) {
                R.id.action_notodo -> {
                    EventBus.getDefault().post(TodoEvent(Constant.TODO_NO, mType))
                    println(111)
                    true
                }
                R.id.action_completed -> {
                    EventBus.getDefault().post(TodoEvent(Constant.TODO_DONE, mType))
                    println(222)
                    true
                }
                else -> {
                    false
                }
            }
        }
}