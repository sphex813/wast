import com.example.wast.databinding.SearchHistoryItemBinding
import com.example.wast.search.HistoryClickListener
import com.example.wast.viewHolders.BaseViewHolder

class SearchHistoryViewHolder(
    val binding: SearchHistoryItemBinding,
    val listener: HistoryClickListener,
) : BaseViewHolder<String>(binding.root) {

    override fun bind(item: String) {
        binding.historyItem = item
        binding.clickListener = listener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }
}