package com.aws;

import software.amazon.awscdk.core.App;

public class AwsCdkApp {
    public static void main(final String[] args) {

        App app = new App();

        VpcStack vpcStack = new VpcStack(app, "Vpc");

        ClusterStack clusterStack = new ClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        RdsStack rdsCdkStack = new RdsStack(app, "Rds", vpcStack.getVpc());
        rdsCdkStack.addDependency(vpcStack);

        SnsStack snsStack = new SnsStack(app, "Sns");

        ServiceProductMsStack serviceProductMsStack = new ServiceProductMsStack(app, "ServiceProductMs", clusterStack.getCluster(), snsStack.getProductEventsTopic());
        serviceProductMsStack.addDependency(clusterStack);
        serviceProductMsStack.addDependency(rdsCdkStack);
        serviceProductMsStack.addDependency(snsStack);

        ServiceLogEventsProductStack serviceLogEventsProductStack = new ServiceLogEventsProductStack(app, "ServiceLogEventsProduct", clusterStack.getCluster(), snsStack.getProductEventsTopic());
        serviceLogEventsProductStack.addDependency(clusterStack);
        serviceLogEventsProductStack.addDependency(snsStack);

        app.synth();
    }
}
