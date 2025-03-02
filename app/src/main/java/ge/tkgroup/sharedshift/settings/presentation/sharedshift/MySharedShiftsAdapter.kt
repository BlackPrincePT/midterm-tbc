package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.databinding.MySharedShiftsViewHolderBinding

class MySharedShiftsAdapter(private val onClicked: (String) -> Unit) :
    ListAdapter<SharedShift, MySharedShiftsAdapter.ItemViewHolder>(ITEM_COMPARATOR) {

    inner class ItemViewHolder(private val binding: MySharedShiftsViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sharedShift: SharedShift) = with(binding) {
            tvTitle.text = sharedShift.companies.toString()

            root.setOnClickListener {
                onClicked.invoke(sharedShift.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = MySharedShiftsViewHolderBinding.inflate(layoutInflater, parent, false)

        return ItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val sharedShift = getItem(position)
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