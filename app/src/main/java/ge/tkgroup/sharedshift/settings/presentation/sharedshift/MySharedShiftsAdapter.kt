package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.databinding.MySharedShiftsViewHolderBinding

class MySharedShiftsAdapter(private val callback: (Callback) -> Unit) :
    PagingDataAdapter<SharedShift, MySharedShiftsAdapter.ItemViewHolder>(ITEM_COMPARATOR) {

    sealed interface Callback {
        data class OnClicked(val sharedShiftId: String) : Callback
        data class OnLongClicked(val sharedShift: SharedShift) : Callback
    }

    inner class ItemViewHolder(private val binding: MySharedShiftsViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sharedShift: SharedShift) = with(binding) {
            tvTitle.text = sharedShift.companies.toString()

            root.setOnClickListener {
                callback.invoke(Callback.OnClicked(sharedShift.id))
            }

            root.setOnLongClickListener {
                callback.invoke(Callback.OnLongClicked(sharedShift))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = MySharedShiftsViewHolderBinding.inflate(layoutInflater, parent, false)

        return ItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val sharedShift = getItem(position) ?: return
        holder.bind(sharedShift)
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<SharedShift>() {
    override fun areItemsTheSame(oldItem: SharedShift, newItem: SharedShift): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SharedShift, newItem: SharedShift): Boolean {
        return oldItem == newItem
    }

}