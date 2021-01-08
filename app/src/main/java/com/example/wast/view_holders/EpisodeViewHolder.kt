import com.example.wast.api.models.SccData
import com.example.wast.databinding.EpisodeBinding
import com.example.wast.search.MovieClickListener
import com.example.wast.utils.HelpUtils
import com.example.wast.view_holders.BaseViewHolder

class EpisodeViewHolder(
    val binding: EpisodeBinding,
    val listener: MovieClickListener
) : BaseViewHolder<SccData>(binding.root) {
    fun bind(item: SccData, position: Int) {
        binding.movie = item
        binding.title = (position + 1).toString() + " " + HelpUtils.getTitle(item._source.i18n_info_labels)
        binding.movieImage = HelpUtils.getMovieLink(item._source.i18n_info_labels)
        binding.clickListner = listener
        binding.executePendingBindings()
    }

    override fun bind(item: SccData) {
        TODO("Not yet implemented")
    }
}