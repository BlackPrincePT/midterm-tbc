package ge.tkgroup.sharedshift.employees.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.databinding.WorkDayViewHolderBinding

class WorkDaysAdapter(private val callback: (Callback) -> Unit) :
    ListAdapter<WorkDay, WorkDaysAdapter.WorkDayViewHolder>(ITEM_COMPARATOR) {

    sealed interface Callback {
        data class OnClicked(val workDay: WorkDay) : Callback
    }

    inner class WorkDayViewHolder(private val binding: WorkDayViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workDay: WorkDay) = with(binding) {
            tvDate.text = workDay.date.toString()
            tvAttendance.text = workDay.hoursWorkedString

            val companies = workDay.exclusiveShift.keys

            tvCompany.text = if (companies.isNotEmpty()) companies.toString() else ""

            root.setOnClickListener {
                callback.invoke(Callback.OnClicked(workDay))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkDayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = WorkDayViewHolderBinding.inflate(layoutInflater, parent, false)

        return WorkDayViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: WorkDayViewHolder, position: Int) {
        val workDay = getItem(position)
        holder.bind(workDay)
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<WorkDay>() {
    override fun areItemsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
        return oldItem == newItem
    }
}