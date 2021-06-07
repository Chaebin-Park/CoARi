package com.cse.coari.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.cse.coari.R
import com.cse.coari.adapter.HofRecyclerAdapter
import com.cse.coari.data.HofData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_emp.*

class EmpActivity : AppCompatActivity() {

    private val entries = ArrayList<BarEntry>()
    private val hofList = arrayListOf<HofData>(
        HofData("deu_logo", "2020년 졸업생 최o용", "네이버", "포털 및 기타 인터넷 정보매개 서비스업", "ctx"),
        HofData("deu_logo", "2020년 졸업생 정O훈", "안랩", "안티 바이러스를 포함해 컴퓨터 전반의 보안 솔루션을 제작 및 관리하는 정보보안업체", "ctx"),
        HofData("deu_logo", "2019년 졸업생 이O혁", "BNK부산은행", "부산은행은 BNK금융그룹의 자회사로 대한민국의 지방은행 중 가장 규모가 큼", "ctx"),
        HofData("deu_logo", "2019년 졸업생 서O영", "롯데정보통신", "정보시스템과 관련된 설비, 인력, 하드웨어 등을 위탁 관리, 운영하는 SM사업부문과 정보시스템 구축 계획 및 운영 전략을 기획하고 시스템 설계 및 개발을 지원하는 SI사업부문이 있는 롯데그룹 계열 SI업체", "ctx"),
        HofData("deu_logo", "2019년 졸업생 진O현", "KIM&CHANG", "디지털 포렌식 연구원", "ctx"),
        HofData("deu_logo", "2019년 졸업생 박O혜", "DOUZONE", "플랫폼 서비스 개발", "ctx"),
        HofData("deu_logo", "2019년 졸업생 김O휘", "SEUN", "전산직", "ctx")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp)
        setBarChart()

        val adapter = HofRecyclerAdapter(this, hofList)
        hof_recyclerview.adapter = adapter

        val layout = GridLayoutManager(this, 3)
        hof_recyclerview.layoutManager = layout
        hof_recyclerview.setHasFixedSize(true)
    }

    private fun setBarChart(){
        entries.add(BarEntry(1.2f, 76.3f)) // 2014년
        entries.add(BarEntry(2.2f, 54.0f)) // 2015년
        entries.add(BarEntry(3.2f, 61.1f)) // 2016년
        entries.add(BarEntry(4.2f, 75.0f)) // 2017년
        entries.add(BarEntry(5.2f, 67.2f)) // 2018년
        entries.add(BarEntry(6.2f, 68.3f)) // 2019년
        entries.add(BarEntry(7.2f, 76.3f)) // 2020년

        emp_chart.run {
            description.isEnabled = false   //차트 옆에 별도로 표기되는 description
            setMaxVisibleValueCount(7)      // 최대 보이는 그래프 개수를 7개
            setPinchZoom(false)             // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false)         //그래프의 그림자
            setDrawGridBackground(false)    //격자구조 넣을건지
            axisLeft.run {                  //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = 101f  //100 위치에 선을 그리기 위해 101f로 맥시멈을 정해주었다
                axisMinimum = 0f    // 최소값 0
                granularity = 50f   // 50 단위마다 선을 그리려고 granularity 설정 해 주었다.
                //위 설정이 20f였다면 총 5개의 선이 그려졌을 것
                setDrawLabels(true)     // 값 적는거 허용 (0, 50, 100)
                setDrawGridLines(true)  //격자 라인 활용
                setDrawAxisLine(false)  // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(context,R.color.deu) // 축 색깔 설정
                gridColor = ContextCompat.getColor(context,R.color.deu)     // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(context,R.color.black)   // 라벨 텍스트 컬러 설정
                textSize = 14f //라벨 텍스트 크기
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM//X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context,R.color.black) //라벨 색상
                valueFormatter = MyXAxisFormatter() // 축 라벨 값 바꿔주기 위함
                textSize = 14f // 텍스트 크기
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }

        val set = BarDataSet(entries,"DataSet")//데이터셋 초기화 하기
        set.color = ContextCompat.getColor(this,R.color.deu)

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.3f//막대 너비 설정하기
        emp_chart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setFitBars(true)
            invalidate()
        }
    }

    inner class MyXAxisFormatter : ValueFormatter(){
        private val days = arrayOf("14년","15년","16년","17년","18년","19년","20년")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
}