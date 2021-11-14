package com.aws;

import software.amazon.awscdk.core.App;

public class AwsCdkApp {
    public static void main(final String[] args) {

        App app = new App();

        VpcStack vpcStack = new VpcStack(app, "Vpc");

        ClusterStack clusterStack = new ClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        RdsCdkStack rdsCdkStack = new RdsCdkStack(app, "Rds", vpcStack.getVpc());
        rdsCdkStack.addDependency(vpcStack);

        ServiceProductMsStack serviceProductMsStack = new ServiceProductMsStack(app, "ServiceProductMs", clusterStack.getCluster());
        serviceProductMsStack.addDependency(clusterStack);
        serviceProductMsStack.addDependency(rdsCdkStack);

        app.synth();
    }
}
