import android.content.Context
import android.text.format.Formatter
import com.example.wast.api.models.StreamInfo
import com.example.wast.databinding.StreamSelectItemBinding
import com.example.wast.dialog.StreamInfoClickListener
import com.example.wast.view_holders.BaseViewHolder

class StreamSelectViewHolder(
    val binding: StreamSelectItemBinding,
    val listener: StreamInfoClickListener,
    val context: Context,
) : BaseViewHolder<StreamInfo>(binding.root) {

    override fun bind(item: StreamInfo) {
        binding.streamInfo = item
        binding.fileSize = Formatter.formatFileSize(context, item.size!!.toLong());
        binding.clickListener = listener
        binding.executePendingBindings()
    }
}