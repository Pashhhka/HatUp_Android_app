package com.example.hatup.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatup.R;
import com.example.hatup.model.Event;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events = new ArrayList<>();
    private List<Long> favoriteArtistIds = new ArrayList<>();
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onImageClick(Event event);
        void onLocationClick(Event event);
        void onLikeClick(Event event, MaterialButton likeButton);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<Event> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    public void setFavoriteArtistIds(List<Long> ids) {
        this.favoriteArtistIds = ids;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = events.get(position);
        holder.tvTitle.setText(currentEvent.title);
        holder.tvAddress.setText(currentEvent.address);

        boolean isFav = favoriteArtistIds.contains(currentEvent.artistId);

        holder.btnLike.setIconResource(isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.ic_btn_speak_now);

        holder.ivBackground.setOnClickListener(v -> {
            if (listener != null) listener.onImageClick(currentEvent);
        });

        holder.btnLocation.setOnClickListener(v -> {
            if (listener != null) listener.onLocationClick(currentEvent);
        });

        holder.btnLike.setOnClickListener(v -> {
            // Передаем клик во фрагмент. Адаптер сам перерисуется, когда БД обновится!
            if (listener != null) listener.onLikeClick(currentEvent, holder.btnLike);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBackground;
        private TextView tvTitle;
        private TextView tvAddress;
        private MaterialButton btnLike;
        private MaterialButton btnLocation;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackground = itemView.findViewById(R.id.iv_event_background);
            tvTitle = itemView.findViewById(R.id.tv_event_title);
            tvAddress = itemView.findViewById(R.id.tv_event_address);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnLocation = itemView.findViewById(R.id.btn_location);
        }
    }
}