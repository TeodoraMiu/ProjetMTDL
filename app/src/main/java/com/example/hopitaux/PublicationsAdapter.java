package com.example.hopitaux;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PublicationsAdapter extends RecyclerView.Adapter<PublicationsAdapter.PublicationsViewHolder>{
    public ArrayList<Announcement> announcements;
    public String role;

    public PublicationsAdapter(ArrayList<Announcement> announcements, String role) {
        this.announcements = announcements;
        this.role = role;
    }

    @NonNull
    @Override
    public PublicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item, parent, false);
        return new PublicationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationsViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        String announcementTitle = announcements.get(position).getTitle();
        holder.announcementTitle.setText(announcementTitle);

        String announcementBloodType = announcements.get(position).getBloodType();
        holder.announcementBloodType.setText(announcementBloodType);

        String announcementReason = announcements.get(position).getReason();
        holder.announcementReason.setText(announcementReason);

        String announcementDescription = announcements.get(position).getDescription();
        holder.announcementDescription.setText(announcementDescription);

        String announcementHospital = announcements.get(position).getHospital();
        holder.announcementHospital.setText(announcementHospital);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role == "user") {
                    // view possible appointment times and dates
                    Donor donor = Donor.getInstance();
                    donor.setSelectedAnnouncementTitle(announcementTitle);
                    Intent intent = new Intent(view.getContext(), ProgramMeetup.class);
                    view.getContext().startActivity(intent);
                } else if (role == "hospital") {
                    // add possible appointment times and dates
                    Hospital hospital = Hospital.getInstance();
                    hospital.setSelectedAnnouncement(announcementTitle);
//                    Intent intent = new Intent(view.getContext(), AddAppointmentIntervals.class);
                    Intent intent = new Intent(view.getContext(), ViewConfirmationRequests.class);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public class PublicationsViewHolder extends RecyclerView.ViewHolder {
        private TextView announcementTitle;
        private TextView announcementBloodType;
        private TextView announcementReason;
        private TextView announcementDescription;
        private TextView announcementHospital;

        public PublicationsViewHolder(@NonNull View itemView) {
            super(itemView);

            announcementTitle = itemView.findViewById(R.id.announcementTitle);
            announcementBloodType = itemView.findViewById(R.id.appointmentDay);
            announcementReason = itemView.findViewById(R.id.appointmentHour);
            announcementDescription = itemView.findViewById(R.id.announcementDescription);
            announcementHospital = itemView.findViewById(R.id.appointmentPlaces);
        }
    }
}
