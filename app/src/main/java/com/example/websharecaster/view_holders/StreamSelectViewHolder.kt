import android.content.Context
import android.text.format.Formatter
import com.example.websharecaster.api.models.StreamInfo
import com.example.websharecaster.databinding.StreamSelectItemBinding
import com.example.websharecaster.dialog.StreamInfoClickListener
import com.example.websharecaster.view_holders.BaseViewHolder

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