import com.example.wast.api.models.SccData
import com.example.wast.databinding.SeriesBinding
import com.example.wast.search.MovieClickListener
import com.example.wast.utils.HelpUtils
import com.example.wast.viewHolders.BaseViewHolder

class SeriesViewHolder(
    val binding: SeriesBinding,
    val listener: MovieClickListener,
) : BaseViewHolder<SccData>(binding.root) {
    override fun bind(item: SccData) {
        binding.movie = item
        binding.title = HelpUtils.getTitle(item._source.i18n_info_labels)
        binding.movieImage = HelpUtils.getMovieLink(item._source.i18n_info_labels)
        binding.clickListner = listener
        binding.executePendingBindings()
    }
}
