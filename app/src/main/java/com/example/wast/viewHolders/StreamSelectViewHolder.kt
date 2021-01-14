import android.content.Context
import android.text.format.Formatter
import com.example.wast.api.models.SccVideoInfo
import com.example.wast.api.models.StreamInfo
import com.example.wast.databinding.StreamSelectItemBinding
import com.example.wast.dialog.StreamInfoClickListener
import com.example.wast.viewHolders.BaseViewHolder

class StreamSelectViewHolder(
    val binding: StreamSelectItemBinding,
    val listener: StreamInfoClickListener,
    val context: Context,
) : BaseViewHolder<StreamInfo>(binding.root) {

    override fun bind(item: StreamInfo) {
        binding.streamInfo = item

        binding.resolution = getResolutionString(item.video)

        binding.fileSize = item.size?.toLong()?.let { Formatter.formatFileSize(context, it) };
        binding.clickListener = listener
        binding.executePendingBindings()
    }

    private fun getResolutionString(
        video: List<SccVideoInfo>?,
    ): String {
        if (video?.get(0)?.width != null && video[0].height != null) {
            return video[0].width?.toInt().toString() + "x" + video[0].height?.toInt().toString()
        }
        return ""
    }
}