package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.databinding.UserViewHolderBinding

class UsersAdapter(private val callback: (Callback) -> Unit) :
    PagingDataAdapter<User, UsersAdapter.UserViewHolder>(ITEM_COMPARATOR) {

    sealed interface Callback {
        data class OnClicked(val userId: String) : Callback
        data class OnLongClicked(val userId: String) : Callback
    }

    inner class UserViewHolder(private val binding: UserViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) = with(binding) {
            tvUsername.text = user.username

            root.setOnClickListener {
                callback.invoke(Callback.OnClicked(userId = user.id))
            }

            root.setOnLongClickListener {
                callback.invoke(Callback.OnLongClicked(userId = user.id))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = UserViewHolderBinding.inflate(layoutInflater, parent, false)

        return UserViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position) ?: return
        holder.bind(user)
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}