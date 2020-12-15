package vn.edu.baitapthuchanhandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import vn.edu.baitapthuchanhandroid.MainActivity;
import vn.edu.baitapthuchanhandroid.PlayMusicActivity;
import vn.edu.baitapthuchanhandroid.R;
import vn.edu.baitapthuchanhandroid.entities.Music;

public class MusicItemAdapter extends RecyclerView.Adapter<MusicItemAdapter.MusicItemViewHoler> {
    public static final ArrayList<Music> musics = new ArrayList<>();
    private LayoutInflater layoutInflater;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MusicItemAdapter(Context context) {
        this.context = context;
        musics.add(new Music(R.raw.ailatrieuphu, "Không có", "Nhạc nền Ai là triệu phú", R.drawable.ailatrieuphu));
        musics.add(new Music(R.raw.buonvuongmauao_nguyenhung, "Nguyễn Hưng", "Buồn vương màu áo", R.drawable.khongbiet));
        musics.add(new Music(R.raw.nangamxadan, "Sơn Tùng MTP", "Nắng ấm xa dần", R.drawable.park_shin_hye));
        musics.add(new Music(R.raw.amthambenem, "Sơn Tùng MTP", "Âm thầm bên em", R.drawable.khuonmatdangthuong));
        musics.add(new Music(R.raw.chungtakhongthuocvenhau, "Sơn Tùng MTP", "Chúng ta không thuộc về nhau", R.drawable.chungtakhuongthuocvenhau));
        musics.add(new Music(R.raw.lactroi, "Sơn Tùng MTP", "Lạc trôi", R.drawable.khuonmatdangthuong));
        musics.add(new Music(R.raw.emcuangayhomqua, "Sơn Tùng MTP", "Em của ngày hôm qua", R.drawable.emcuangayhomqua));
        musics.add(new Music(R.raw.khuonmatdangthuong, "Sơn Tùng MTP", "Khuôn mặt đáng thương", R.drawable.khuonmatdangthuong));
        musics.add(new Music(R.raw.noinaycoanh, "Sơn Tùng MTP", "Nơi này có anh", R.drawable.emcuangayhomqua));

        musics.sort(new Comparator<Music>() {
            @Override
            public int compare(Music music, Music t1) {
                return music.getName().compareTo(t1.getName());
            }
        });
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MusicItemViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_music, parent, false);
        return new MusicItemViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicItemViewHoler holder, final int position) {
        holder.avatar.setImageResource(musics.get(position).getAvatar());
        holder.tvSinger.setText(musics.get(position).getSingerName());
        holder.tvName.setText(musics.get(position).getName());
        holder.avatar.setClipToOutline(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("music", position);
                context.startActivity(intent);
            }
        });
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
