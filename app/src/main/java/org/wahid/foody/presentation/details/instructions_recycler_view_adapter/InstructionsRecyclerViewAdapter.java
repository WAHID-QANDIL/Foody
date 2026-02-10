package org.wahid.foody.presentation.details.instructions_recycler_view_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wahid.foody.R;

import java.util.List;

public class InstructionsRecyclerViewAdapter extends RecyclerView.Adapter<InstructionsRecyclerViewAdapter.InstructionViewHolder> {

    private List<String> instructions;

    public List<String> getInstructions() {
        return instructions;
    }

    public void updateAndNotifyListItems(List<String> updatedInstructions) {
        this.instructions = updatedInstructions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_recycler_view_item, parent,false);
        return new InstructionsRecyclerViewAdapter.InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {

        String model = instructions.get(position);
        holder.stepNumber.setText("STEP: "+(position+1));
        holder.stepDetails.setText(model);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public static class InstructionViewHolder extends RecyclerView.ViewHolder{
        TextView stepNumber;
        TextView stepDetails;
        CheckBox checkBox;
        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View item){
            stepDetails = item.findViewById(R.id.tv_instruction_details);
            stepNumber = item.findViewById(R.id.tv_instruction_step_number);
            checkBox = item.findViewById(R.id.cb_instruction_item);
        }
    }
}
