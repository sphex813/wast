import com.example.wast.api.models.SccData
import com.example.wast.databinding.MovieDetailBinding
import com.example.wast.search.MovieClickListener
import com.example.wast.utils.HelpUtils
import com.example.wast.viewHolders.BaseViewHolder

class PosterViewHolder(
    val binding: MovieDetailBinding,
    val listener: MovieClickListener,
) :
    BaseViewHolder<SccData>(binding.root) {
    override fun bind(item: SccData) {
        binding.movie = item
        binding.movieImage = HelpUtils.getMovieImageLink(item._source.i18n_info_labels)
        binding.clickListner = listener
        binding.executePendingBindings()
    }
}