package ge.tkgroup.sharedshift.employees.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.databinding.EmployeeViewHolderBinding

class EmployeesAdapter(private val callback: (Callback) -> Unit) :
    PagingDataAdapter<Employee, EmployeesAdapter.EmployeeViewHolder>(ITEM_COMPARATOR) {

    sealed interface Callback {
        data class OnClicked(val employeeId: String) : Callback
    }

    inner class EmployeeViewHolder(private val binding: EmployeeViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: Employee) = with(binding) {
            tvFullName.text = employee.fullName
            tvOccupation.text = employee.occupation

            root.setOnClickListener {
                callback.invoke(Callback.OnClicked(employee.id))
            }
        }
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = getItem(position) ?: return
        holder.bind(employee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = EmployeeViewHolderBinding.inflate(layoutInflater, parent, false)

        return EmployeeViewHolder(viewBinding)
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Employee>() {
    override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem == newItem
    }
}