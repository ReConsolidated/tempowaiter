package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.table.TableInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WaiterService {

    public void callToTable(TableInfo tableInfo) {
        System.out.println("Call to table: " + tableInfo.getTableId());
    }
}
