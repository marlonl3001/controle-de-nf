package br.com.mdr.controledenf.utils

import br.com.mdr.controledenf.utils.extension.toCurrencyString
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class PieChartFormatter: ValueFormatter() {
    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return value.toDouble().toCurrencyString()
    }
}
