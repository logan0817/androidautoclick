package com.logan.androidautoclick.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.logan.androidautoclick.R
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import kotlinx.android.synthetic.main.activity_pinlockview.pattern_indicator_view
import kotlinx.android.synthetic.main.activity_pinlockview.pattern_lock_view
import java.util.Calendar
import java.util.Date

class PinLockActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinlockview)
        /*        Blurry.with(ivBackground.context)
                    .sampling(3)
                    .animate(500)
                    .from(BitmapFactory.decodeResource(resources, R.drawable.img_anxin_shu))
                    .into(ivBackground)*/
        pattern_lock_view.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {
                pattern_indicator_view.updateState(hitIndexList, false)
            }

            override fun onClear(view: PatternLockerView) {
                pattern_indicator_view.updateState(listOf(), false)
            }

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val dateObject = Date()
                val calendarInstance = Calendar.getInstance()
                calendarInstance.time = dateObject
                val hour = calendarInstance.get(Calendar.HOUR)
                val minute = calendarInstance.get(Calendar.MINUTE)
                val ampm = if (calendarInstance.get(Calendar.AM_PM) == 0) "AM " else "PM "
                val date = calendarInstance.get(Calendar.DATE)
                val month = calendarInstance.get(Calendar.MONTH) + 1
                val year = calendarInstance.get(Calendar.YEAR)
                val result = (month + date)
                val list = if (result > 10) {
                    result.toString().toList().map { it.toString().toInt() - 1 }
                } else {
                    listOf(result - 1)
                }
                if (list.containsAll(hitIndexList)) {
                    Toast.makeText(this@PinLockActivity, "解锁成功", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@PinLockActivity, EditSettingsActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@PinLockActivity, "密码错误", Toast.LENGTH_LONG).show()
                }
            }

            override fun onStart(view: PatternLockerView) {

            }
        })
    }
}