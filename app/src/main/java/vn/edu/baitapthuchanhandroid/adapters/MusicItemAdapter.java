package vn.edu.baitapthuchanhandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.baitapthuchanhandroid.R;
import vn.edu.baitapthuchanhandroid.entities.Music;

public class MusicItemAdapter extends RecyclerView.Adapter<MusicItemAdapter.MusicItemViewHoler> {
    private final ArrayList<Music> musics = new ArrayList<>();
    private LayoutInflater layoutInflater;
    public MusicItemAdapter(Context context) {
        musics.add(new Music(R.raw.ailatrieuphu, "Không có", "Nhạc nền Ai là triệu phú", R.drawable.park_shin_hye));
        musics.add(new Music(R.raw.buonvuongmauao_nguyenhung, "Nguyễn Hưng", "Buồn vương màu áo", R.drawable.park_shin_hye));
        musics.add(new Music(R.raw.nangamxadan, "Sơn Tùng MTP", "Nắng ấm xa dần", R.drawable.park_shin_hye));
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MusicItemViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_music, parent, false);
        return new MusicItemViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicItemViewHoler holder, int position) {
        holder.avatar.setImageResource(musics.get(position).getAvatar());
        holder.tvSinger.setText(musics.get(position).getSingerName());
        holder.tvName.setText(musics.get(position).getName());
        holder.avatar.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class MusicItemViewHoler extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView tvName, tvSinger;
        public MusicItemViewHoler(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_main_activity);
            tvName = itemView.findViewById(R.id.name_main_activity);
            tvSinger = itemView.findViewById(R.id.singer_main_activity);
        }
    }
}