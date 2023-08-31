package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.table.TableInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WaiterService {
    private final WaiterRequestRepository waiterRequestRepository;

    public void callToTable(TableInfo tableInfo) {
        WaiterRequest request = new WaiterRequest();

        System.out.println("Call to table: " + tableInfo.getTableId());
    }

    public List<WaiterRequest> getRequests(Long companyId) {
        return null;
    }
}
