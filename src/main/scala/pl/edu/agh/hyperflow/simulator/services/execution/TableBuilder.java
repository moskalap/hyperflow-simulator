package pl.edu.agh.hyperflow.simulator.services.execution;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import pl.edu.agh.hyperflow.simulator.cloud.ContainerTask;

import java.util.List;

public class TableBuilder extends CloudletsTableBuilder {

    public TableBuilder(List<? extends Cloudlet> list) {
        super(list);
        this.addColumn(getTable().addColumn("Nameg "), cloudlet -> ((ContainerTask) cloudlet).name());
    }



}
