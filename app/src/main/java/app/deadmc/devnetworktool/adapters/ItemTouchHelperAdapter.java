package app.deadmc.devnetworktool.adapters;

/**
 * Created by Feren on 24.09.2016.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
