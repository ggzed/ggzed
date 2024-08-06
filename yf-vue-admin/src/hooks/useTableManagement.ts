import {TableInstance} from "element-plus";
import {Ref} from "vue";

interface TableManagement<T> {
    /**
     * 选中表单的Id集合
     */
    selectedIds: Ref<T[]>;

    /**
     * 处理选中项变化
     * @param newSelection 新选择数据
     */
    handleSelectionChange(newSelection: any[]): void;

    /**
     * 处理单元格双击事件
     * @param row 双击数据
     */
    handleCellDblclick(row: any): void;
}

export function useTableManagement<T>(
    tableRef: Ref<TableInstance | null>
): TableManagement<T> {
    const selectedIds = ref<T[]>([]) as Ref<T[]>;

    function handleCellDblclick(row: any) {
        const isSelected = selectedIds.value.includes(row.id);
        tableRef.value?.toggleRowSelection(row, !isSelected);
    }

    function handleSelectionChange(newSelection: any[]) {
        selectedIds.value = newSelection.map(item => item.id);
    }

    return {
        selectedIds,
        handleCellDblclick,
        handleSelectionChange
    };
}
