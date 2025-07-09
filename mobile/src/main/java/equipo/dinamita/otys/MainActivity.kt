package equipo.dinamita.otys

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: SensorPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Verifica que tengas ViewPager2 y botones en este layout

        // Inicializar ViewPager2
        viewPager = findViewById(R.id.viewPagerSensors)

        // Lista de sensores de ejemplo
        val sensors = listOf(
            SensorData("Ritmo Cardíaco", 72),
            SensorData("Oxígeno", 95),
            SensorData("Temperatura", 37)
        )

        adapter = SensorPagerAdapter(sensors)
        viewPager.adapter = adapter

        // Enlazar botones de navegación para cambiar sensores
        val btnPrev = findViewById<ImageButton>(R.id.btnPrev)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)

        btnPrev.setOnClickListener {
            val prev = if (viewPager.currentItem - 1 < 0) adapter.itemCount - 1 else viewPager.currentItem - 1
            viewPager.currentItem = prev
        }

        btnNext.setOnClickListener {
            val next = (viewPager.currentItem + 1) % adapter.itemCount
            viewPager.currentItem = next
        }
    }

    // Evita que se infle menú arriba (los tres puntos)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    // Adapter para ViewPager2
    class SensorPagerAdapter(
        private val sensors: List<SensorData>
    ) : RecyclerView.Adapter<SensorPagerAdapter.SensorViewHolder>() {

        inner class SensorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val pieChart: PieChart = view.findViewById(R.id.pieChart)
            val tvSensorValue: TextView = view.findViewById(R.id.tvSensorValue)
            val tvSensorName: TextView = view.findViewById(R.id.tvSensorName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sensor_chart, parent, false)
            return SensorViewHolder(view)
        }

        override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
            val sensor = sensors[position]

            setupPieChart(holder.pieChart, sensor.value)

            holder.tvSensorValue.text = sensor.value.toString()
            holder.tvSensorName.text = sensor.name
        }

        override fun getItemCount(): Int = sensors.size

        private fun setupPieChart(chart: PieChart, value: Int) {
            chart.setUsePercentValues(false)
            chart.description.isEnabled = false
            chart.setDrawHoleEnabled(true)
            chart.setHoleColor(Color.WHITE)
            chart.holeRadius = 75f
            chart.transparentCircleRadius = 80f
            chart.setDrawCenterText(true)   // Aquí puedes activar para mostrar texto en el centro
            chart.centerText = "$value"
            chart.setCenterTextSize(36f)
            chart.setCenterTextColor(Color.BLACK)
            chart.legend.isEnabled = false
            chart.setTouchEnabled(false)

            val entries = ArrayList<PieEntry>()
            entries.add(PieEntry(value.toFloat()))
            entries.add(PieEntry((100 - value).toFloat()))

            val dataSet = PieDataSet(entries, "")
            dataSet.setDrawValues(false)
            dataSet.colors = listOf(Color.parseColor("#3F51B5"), Color.LTGRAY)

            val data = PieData(dataSet)
            chart.data = data
            chart.invalidate()
        }
    }

}

// Modelo de datos para sensores
data class SensorData(
    val name: String,
    val value: Int
)
