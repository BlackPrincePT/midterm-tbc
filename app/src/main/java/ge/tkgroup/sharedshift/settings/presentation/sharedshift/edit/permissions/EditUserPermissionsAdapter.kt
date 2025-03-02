package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.permissions

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.Permission
import java.util.UUID

class EditUserPermissionsAdapter(private val callbacks: (Callback) -> Unit) :
    ListAdapter<EditUserPermissionsAdapter.Item, EditUserPermissionsAdapter.ItemViewHolder>(
        ITEM_COMPARATOR
    ) {

    data class Item(
        val id: UUID = UUID.randomUUID(),
        val permission: Permission,
        val status: Boolean = false
    )

    sealed interface Callback {
        data class OnStatusChanged(val id: UUID, val status: Boolean) : Callback
    }

    inner class ItemViewHolder(val checkBox: AppCompatCheckBox) :
        RecyclerView.ViewHolder(checkBox)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val checkBox = AppCompatCheckBox(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ItemViewHolder(checkBox)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.checkBox.text = item.permission.name
        holder.checkBox.isChecked = item.status
        holder.checkBox.setOnCheckedChangeListener { _, currentValue ->
            callbacks.invoke(Callback.OnStatusChanged(id = item.id, status = currentValue))
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<EditUserPermissionsAdapter.Item>() {
    override fun areItemsTheSame(
        oldItem: EditUserPermissionsAdapter.Item,
        newItem: EditUserPermissionsAdapter.Item
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EditUserPermissionsAdapter.Item,
        newItem: EditUserPermissionsAdapter.Item
    ): Boolean {
        return oldItem.id == newItem.id
    }
}