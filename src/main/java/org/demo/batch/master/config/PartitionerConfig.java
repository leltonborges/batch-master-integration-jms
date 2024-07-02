package org.demo.batch.master.config;

import org.demo.batch.master.repository.ClientRepository;
import org.demo.batch.master.model.Client;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@JobScope
public class PartitionerConfig
        implements Partitioner {
    private final ClientRepository clientRepository;

    public PartitionerConfig(ClientRepository clientRepository) {

        this.clientRepository = clientRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int i) {
        List<Client> all = this.clientRepository.findAll();
        Map<String, ExecutionContext> map = new HashMap<>();
        int partitionNumber = 0;
        for (Client client : all) {
            ExecutionContext context = new ExecutionContext();
            context.put("client", client.getSeqClient());
            context.put("clientName", client.getName());
            map.put("partition" + partitionNumber, context);
            partitionNumber++;
        }
        return map;
    }
}
