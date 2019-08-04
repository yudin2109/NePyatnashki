package com.styudint.nepyatnashki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.styudint.nepyatnashki.data.GameInfo
import kotlinx.android.synthetic.main.statistics_page.*
import kotlinx.android.synthetic.main.stats_row.view.*

class StatisticsPage : FragmentActivity() {
    lateinit var viewModel: StatisticsPageViewModel

    private var views = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_page)

        viewModel = ViewModelProviders.of(this).get(StatisticsPageViewModel::class.java)

        viewModel.statistics.observe(this, Observer { list -> run {
            views.forEach {
                anchor.removeView(it)
            }
            list.forEach {
                anchor.addView(createStatsView(it))
            }
        }})
    }

    private fun formatTimestamp(timestamp: Long): String {
        return timestamp.toString()
    }

    private fun formatTime(time: Long): String = String.format("%d:%02d:%02d", time / 6000, (time / 100) % 60, time % 100)

    private fun createStatsView(info: GameInfo): View {
        val view = LayoutInflater.from(this).inflate(R.layout.stats_row, null)

        view.timestamp.text = formatTimestamp(info.timestamp)
        view.time.text = formatTime(info.time)

        var ended = "No"
        view.ended.setTextColor(resources.getColor(R.color.red))
        if (info.ended) {
            ended = "Yes"
            view.ended.setTextColor(resources.getColor(R.color.green))
        }
        view.ended.text = ended

        return view
    }
}