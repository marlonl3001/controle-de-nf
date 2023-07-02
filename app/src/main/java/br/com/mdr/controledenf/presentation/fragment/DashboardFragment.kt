package br.com.mdr.controledenf.presentation.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.mdr.base.domain.Expense
import br.com.mdr.base.domain.Invoice
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.databinding.FragmentDashboardBinding
import br.com.mdr.controledenf.presentation.viewmodel.AuthViewModel
import br.com.mdr.controledenf.presentation.viewmodel.HistoryViewModel
import br.com.mdr.controledenf.utils.PieChartFormatter
import br.com.mdr.controledenf.utils.extension.showBottomSheet
import br.com.mdr.controledenf.utils.extension.toHtmlSpanned
import br.com.mdr.controledenf.utils.safeLet
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Calendar

class DashboardFragment : Fragment() {
    private var binding: FragmentDashboardBinding? = null
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val viewModel: HistoryViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding?.apply {
            btnAddExpense.setOnClickListener {
                showExpenseRegisterFragment()
            }
            btnAddInvoice.setOnClickListener {
                showInvoiceRegisterFragment()
            }
            imgFilter.setOnClickListener {
                showYearFilter()
            }
        }
    }

    private fun setupViews() {
        binding?.apply {
            configurePieChart(chartLimitAvailable, R.string.available_billing)
            configurePieChart(chartExpenses, R.string.expenses)
            configurePieChart(chartInvoices, R.string.invoice_entries)
            configurePieChart(chartExpensesByCategory, R.string.expenses_by_category)
            viewModel.currentYear = Calendar.getInstance().get(Calendar.YEAR)
            setYearFilter(viewModel.currentYear)
        }
    }

    private fun setYearFilter(year: Int) {
        binding?.txtYearFilter?.text =
            resources.getString(R.string.dashboard_year_filter, year).toHtmlSpanned()
    }

    private fun showYearFilter() {
        val bottomSheet = YearFilterFragment().apply {
            yearClick = { year ->
                viewModel.currentYear = year.toIntOrNull() ?: viewModel.currentYear
                setYearFilter(viewModel.currentYear)
            }
        }
        showBottomSheet(bottomSheet)
    }

    private fun showExpenseRegisterFragment() {
        val bottomSheet = RegisterExpenseFragment.newInstance().apply {
            registerClick = { e ->
                viewModel.saveExpense(e)
            }
        }
        showBottomSheet(bottomSheet)
    }

    private fun showInvoiceRegisterFragment() {
        val bottomSheet = RegisterInvoiceFragment.newInstance().apply {
            registerClick = { i ->
                viewModel.saveInvoice(i)
            }
        }
        showBottomSheet(bottomSheet)
    }

    private fun setupObservers() {
        viewModel.apply {
            expenses.observe(viewLifecycleOwner) {
                setExpenseAmountChart(it)
            }
            invoices.observe(viewLifecycleOwner) {
                setAvailableLimitChart()
                setInvoiceAmountChart(it)
            }
            getExpenses()
            getInvoices()
        }
        authViewModel.company.observe(viewLifecycleOwner) {
            setAvailableLimitChart()
        }
    }

    private fun setInvoiceAmountChart(invoices: List<Invoice>) {
        binding?.chartInvoices?.apply {
            val chartItems = arrayListOf<Pair<Double, String>>()
            val filteredByMonth = invoices
                .filter { it.year == viewModel.currentYear }
                .sortedByDescending { it.month }
                .groupBy { it.month }
            var amountSum = 0.0

            for (items in filteredByMonth) {
                for (item in items.value) {
                    amountSum += item.value
                }
                chartItems.add(Pair(amountSum, items.value.first().month))
                amountSum = 0.0
            }
            val entries = arrayListOf<PieEntry>()
            for (chartItem in chartItems) {
                entries.add(PieEntry(chartItem.first.toFloat(), chartItem.second))
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = getChartColors()

            val data = PieData(dataSet)
            data.setValueFormatter(PieChartFormatter())
            data.setValueTextSize(resources.getDimension(R.dimen.font8))

            visibility = if (entries.isEmpty()) View.GONE else View.VISIBLE
            setData(data)
            highlightValues(null)
            invalidate()
        }
    }

    private fun setExpenseAmountChart(expenses: List<Expense>) {
        val chartItems = arrayListOf<Pair<Double, String>>()
        val filteredByMonth = expenses
            .filter { it.year == viewModel.currentYear }
            .sortedBy { it.month }
            .groupBy { it.month }
        var amountSum = 0.0
        val months = resources.getStringArray(R.array.months)

        for (items in filteredByMonth) {
            for (item in items.value) {
                amountSum += item.value
            }
            chartItems.add(Pair(amountSum, months[items.value.first().month]))
            amountSum = 0.0
        }
        val entries = arrayListOf<PieEntry>()
        for (chartItem in chartItems) {
            entries.add(PieEntry(chartItem.first.toFloat(), chartItem.second))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = getChartColors()

        val data = PieData(dataSet)
        data.setValueFormatter(PieChartFormatter())
        data.setValueTextSize(resources.getDimension(R.dimen.font8))

        binding?.chartExpenses?.apply {
            visibility = if (entries.isEmpty()) View.GONE else View.VISIBLE
            setData(data)
            highlightValues(null)
            invalidate()
        }

        //Expenses by Category
        val filteredByCategory = expenses
            .filter { it.year == viewModel.currentYear }
            .sortedBy { it.category }
            .groupBy { it.category }

        chartItems.clear()
        for (items in filteredByCategory) {
            for (item in items.value) {
                amountSum += item.value
            }
            chartItems.add(Pair(amountSum, items.value.first().category))
            amountSum = 0.0
        }

        val entriesByCategory = arrayListOf<PieEntry>()
        for (chartItem in chartItems) {
            entriesByCategory.add(PieEntry(chartItem.first.toFloat(), chartItem.second))
        }

        val ds = PieDataSet(entriesByCategory, "")
        ds.colors = getChartColors()

        val dataByCategory = PieData(ds)
        dataByCategory.setValueFormatter(PieChartFormatter())
        dataByCategory.setValueTextSize(resources.getDimension(R.dimen.font8))

        binding?.chartExpensesByCategory?.apply {
            visibility = if (entries.isEmpty()) View.GONE else View.VISIBLE
            setData(dataByCategory)
            highlightValues(null)
            invalidate()
        }
    }

    private fun setAvailableLimitChart() {
        safeLet(viewModel.invoices.value, authViewModel.company.value) { invoices, company ->
            binding?.chartLimitAvailable?.apply {

                var limitSpent = 0.0
                for (invoice in invoices.filter { it.year == viewModel.currentYear }) {
                    limitSpent += invoice.value
                }
                val limitAvailable = (company.billingThreshold - limitSpent).toFloat()
                val billingEntry = PieEntry(limitSpent.toFloat(), "Faturamento")
                val availableEntry = PieEntry(limitAvailable, "Limite\nDisponível")

                val entries = listOf(availableEntry, billingEntry)
                val dataSet = PieDataSet(entries, "")
                dataSet.colors = getChartColors()

                val data = PieData(dataSet)
                data.setValueFormatter(PieChartFormatter())
                data.setValueTextSize(resources.getDimension(R.dimen.font8))
                setData(data)
                highlightValues(null)
                invalidate()
            }
        }
    }

    private fun getChartColors(): ArrayList<Int> {
        // add a lot of colors
        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        return colors
    }

    private fun configurePieChart(pieChart: PieChart, centerDescription: Int) {
        pieChart.apply {
            legend.isEnabled = false
            description.isEnabled = false
            rotationAngle = 0f
            isRotationEnabled = false

            setCenterTextTypeface(Typeface.SERIF)
            setCenterTextColor(Color.GRAY)
            centerText = resources.getString(centerDescription)
            setDrawEntryLabels(true)
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(resources.getDimension(R.dimen.font8))
            setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
        }
    }

}