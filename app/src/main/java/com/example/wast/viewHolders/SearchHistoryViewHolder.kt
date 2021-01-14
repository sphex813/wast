import com.example.wast.databinding.SearchHistoryItemBinding
import com.example.wast.search.HistoryClickListener
import com.example.wast.viewHolders.BaseViewHolder

class SearchHistoryViewHolder(
    val binding: SearchHistoryItemBinding,
    val listener: HistoryClickListener,
) : BaseViewHolder<String>(binding.root) {

    fun bind(item: String, position: Int) {
        binding.historyItem = item
        binding.clickListener = listener
        binding.position = position
        binding.executePendingBindings()
    }

    override fun bind(item: String) {
        TODO("Not yet implemented")
    }
}