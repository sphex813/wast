import com.example.websharecaster.api.models.SccData
import com.example.websharecaster.databinding.SeriesBinding
import com.example.websharecaster.search.MovieClickListener
import com.example.websharecaster.utils.HelpUtils
import com.example.websharecaster.view_holders.BaseViewHolder

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
