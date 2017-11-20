package app.deadmc.devnetworktool.fragments.ping;

import java.util.ArrayList;

import app.deadmc.devnetworktool.fragments.BaseFragment;
import app.deadmc.devnetworktool.modules.PingStructure;

/**
 * Created by Feren on 14.01.2017.
 */
abstract public class BasePingFragment extends BaseFragment {
    protected ArrayList<PingStructure> pingStructureArrayList;
    abstract public void addPingStructure(PingStructure pingStructure,boolean canUpdate);
    abstract public void refreshFragment(ArrayList<PingStructure> arrayList);

    public void setPingStructureArrayList(ArrayList<PingStructure> pingStructureArrayList) {
        this.pingStructureArrayList = new ArrayList<>(pingStructureArrayList);
    }
}
