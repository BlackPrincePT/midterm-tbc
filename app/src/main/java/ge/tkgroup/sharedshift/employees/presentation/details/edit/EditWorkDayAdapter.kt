package ge.tkgroup.sharedshift.employees.presentation.details.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.utils.extensions.EMPTY
import ge.tkgroup.sharedshift.databinding.EditWorkDayViewHolderBinding
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class EditWorkDayAdapter(private val callback: (Callback) -> Unit) :
    ListAdapter<EditWorkDayAdapter.Item, EditWorkDayAdapter.EditWorkDayViewHolder>(ITEM_COMPARATOR) {

    data class Item(
        val id: UUID = UUID.randomUUID(),
        var hours: String = String.EMPTY,
        val company: String = String.EMPTY
    )

    sealed interface Callback {
        data object OnAdd : Callback
        data class OnHoursChanged(val id: UUID, val hours: String) : Callback
        data class OnCompanyChanged(val id: UUID, val company: String) : Callback
        data class SetupAdapter(val id: UUID, val spinner: Spinner) : Callback
    }

    inner class EditWorkDayViewHolder(private val binding: EditWorkDayViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, itemPosition: Int) = with(binding) {
            etEquipmentHours.apply {
                setText(item.hours)

                doAfterTextChanged {
                    callback(Callback.OnHoursChanged(id = item.id, hours = it.toString()))
                    if (currentList.lastIndex == itemPosition && !etEquipmentHours.text.isNullOrEmpty())
                        callback(Callback.OnAdd)
                }
            }

            callback(Callback.SetupAdapter(id = item.id, spinner = spinnerCompany))

            spinnerCompany.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    callback(Callback.OnCompanyChanged(id = item.id, company = spinnerCompany.selectedItem as String))
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditWorkDayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = EditWorkDayViewHolderBinding.inflate(layoutInflater, parent, false)

        return EditWorkDayViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: EditWorkDayViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<EditWorkDayAdapter.Item>() {
    override fun areItemsTheSame(
        oldItem: EditWorkDayAdapter.Item,
        newItem: EditWorkDayAdapter.Item
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EditWorkDayAdapter.Item,
        newItem: EditWorkDayAdapter.Item
    ): Boolean {
        return oldItem.id == newItem.id
    }
}