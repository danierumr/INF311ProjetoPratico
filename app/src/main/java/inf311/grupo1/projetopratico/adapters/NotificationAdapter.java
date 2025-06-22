package inf311.grupo1.projetopratico.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import inf311.grupo1.projetopratico.R;
import inf311.grupo1.projetopratico.models.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notifications;
    private OnNotificationClickListener clickListener;
    private OnNotificationActionListener actionListener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public interface OnNotificationActionListener {
        void onMarkAsRead(Notification notification);
        void onDelete(Notification notification);
        void onShowMenu(Notification notification, View anchorView);
    }

    public NotificationAdapter(Context context) {
        this.context = context;
        this.notifications = new ArrayList<>();
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public void addNotification(Notification notification) {
        notifications.add(0, notification); // Adiciona no topo
        notifyItemInserted(0);
    }

    public void updateNotification(Notification notification) {
        int index = findNotificationIndex(notification.getId());
        if (index != -1) {
            notifications.set(index, notification);
            notifyItemChanged(index);
        }
    }

    public void removeNotification(String notificationId) {
        int index = findNotificationIndex(notificationId);
        if (index != -1) {
            notifications.remove(index);
            notifyItemRemoved(index);
        }
    }

    private int findNotificationIndex(String id) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnNotificationActionListener(OnNotificationActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private CardView cardNotification;
        private View priorityIndicator;
        private ImageView ivNotificationIcon;
        private TextView tvNotificationTitle;
        private TextView tvNotificationTime;
        private TextView tvNotificationBody;
        private TextView tvNotificationType;
        private TextView tvNotificationPriority;
        private View unreadIndicator;
        private ImageView ivNotificationAction;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardNotification = itemView.findViewById(R.id.card_notification);
            priorityIndicator = itemView.findViewById(R.id.priority_indicator);
            ivNotificationIcon = itemView.findViewById(R.id.iv_notification_icon);
            tvNotificationTitle = itemView.findViewById(R.id.tv_notification_title);
            tvNotificationTime = itemView.findViewById(R.id.tv_notification_time);
            tvNotificationBody = itemView.findViewById(R.id.tv_notification_body);
            tvNotificationType = itemView.findViewById(R.id.tv_notification_type);
            tvNotificationPriority = itemView.findViewById(R.id.tv_notification_priority);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);
            ivNotificationAction = itemView.findViewById(R.id.iv_notification_action);
        }

        public void bind(Notification notification) {
            // Título
            tvNotificationTitle.setText(notification.getTitle());
            
            // Corpo
            tvNotificationBody.setText(notification.getBody());
            
            // Tempo
            tvNotificationTime.setText(notification.getFormattedTime());
            
            // Tipo
            tvNotificationType.setText(notification.getTypeDisplayName());
            
            // Prioridade
            tvNotificationPriority.setText(notification.getPriority());
            
            // Ícone do tipo
            ivNotificationIcon.setImageResource(notification.getTypeIcon());
            ivNotificationIcon.setColorFilter(notification.getPriorityColor());
            
            // Indicador de prioridade
            priorityIndicator.setBackgroundColor(notification.getPriorityColor());
            
            // Badge de prioridade com cor dinâmica
            setPriorityBadgeStyle(tvNotificationPriority, notification);
            
            // Estado de leitura
            setupReadState(notification);
            
            // Click listeners
            setupClickListeners(notification);
        }

        private void setPriorityBadgeStyle(TextView badge, Notification notification) {
            int backgroundColor;
            int textColor;
            
            switch (notification.getPriority()) {
                case Notification.PRIORITY_URGENT:
                    backgroundColor = ContextCompat.getColor(context, R.color.error_red);
                    textColor = Color.WHITE;
                    break;
                case Notification.PRIORITY_HIGH:
                    backgroundColor = ContextCompat.getColor(context, R.color.warning_orange);
                    textColor = Color.WHITE;
                    break;
                case Notification.PRIORITY_NORMAL:
                    backgroundColor = ContextCompat.getColor(context, R.color.info_blue);
                    textColor = Color.WHITE;
                    break;
                case Notification.PRIORITY_LOW:
                    backgroundColor = ContextCompat.getColor(context, R.color.text_secondary);
                    textColor = Color.WHITE;
                    break;
                default:
                    backgroundColor = ContextCompat.getColor(context, R.color.text_secondary);
                    textColor = Color.WHITE;
                    break;
            }
            
            badge.setBackgroundColor(backgroundColor);
            badge.setTextColor(textColor);
        }

        private void setupReadState(Notification notification) {
            if (notification.isRead()) {
                // Notificação lida
                cardNotification.setCardBackgroundColor(Color.WHITE);
                unreadIndicator.setVisibility(View.GONE);
                tvNotificationTitle.setAlpha(0.8f);
                tvNotificationBody.setAlpha(0.7f);
            } else {
                // Notificação não lida
                cardNotification.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.notification_unread_bg)
                );
                unreadIndicator.setVisibility(View.VISIBLE);
                tvNotificationTitle.setAlpha(1.0f);
                tvNotificationBody.setAlpha(1.0f);
            }
        }

        private void setupClickListeners(Notification notification) {
            // Click no card inteiro
            cardNotification.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onNotificationClick(notification);
                }
            });
            
            // Click no botão de ação
            ivNotificationAction.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onShowMenu(notification, v);
                }
            });
        }
    }
} 