package ge.tkgroup.sharedshift.settings.presentation.sharedshift.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.utils.extensions.EMPTY
import ge.tkgroup.sharedshift.databinding.CreateSharedShiftViewHolderBinding
import java.util.UUID

class CreateSharedShiftAdapter(private val callback: (Callback) -> Unit) :
    ListAdapter<CreateSharedShiftAdapter.Item, CreateSharedShiftAdapter.ItemViewHolder>(
        ITEM_COMPARATOR
    ) {

    data class Item(
        val id: UUID = UUID.randomUUID(),
        var text: String = String.EMPTY
    )

    sealed interface Callback {
        data object OnAdd : Callback
        data class OnTextChanged(val id: UUID, val text: String) : Callback
    }

    inner class ItemViewHolder(private val binding: CreateSharedShiftViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemPosition: Int) = with(binding) {
            val item = getItem(itemPosition)

            etCompanyName.apply {
                setText(item.text)

                doAfterTextChanged {
                    callback(Callback.OnTextChanged(id = item.id, text = it.toString()))

                    btnAdd.isVisible =
                        currentList.lastIndex == itemPosition && !text.isNullOrEmpty()
                }

                btnAdd.isVisible = currentList.lastIndex == itemPosition && !text.isNullOrEmpty()
            }

            btnAdd.apply {
                setOnClickListener {
                    callback(Callback.OnAdd)
                    isVisible = false
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CreateSharedShiftViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<CreateSharedShiftAdapter.Item>() {
    override fun areItemsTheSame(
        oldItem: CreateSharedShiftAdapter.Item,
        newItem: CreateSharedShiftAdapter.Item
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CreateSharedShiftAdapter.Item,
        newItem: CreateSharedShiftAdapter.Item
    ): Boolean {
        return oldItem.id == newItem.id
    }
}