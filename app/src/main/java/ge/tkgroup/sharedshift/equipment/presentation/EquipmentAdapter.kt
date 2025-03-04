package ge.tkgroup.sharedshift.equipment.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.databinding.EquipmentViewHolderBinding

class EquipmentAdapter :
    PagingDataAdapter<Equipment, EquipmentAdapter.EquipmentViewHolder>(ITEM_COMPARATOR) {

    inner class EquipmentViewHolder(private val binding: EquipmentViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(equipment: Equipment) = with(binding) {
            tvEquipment.text = equipment.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = EquipmentViewHolderBinding.inflate(layoutInflater, parent, false)

        return EquipmentViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = getItem(position) ?: return
        holder.bind(equipment)
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Equipment>() {
    override fun areItemsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
        return oldItem == newItem
    }
}