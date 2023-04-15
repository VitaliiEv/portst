package vitaliiev.portst;


/**
 * <p>Port проиндексирован набором последовательностей чисел произвольной длины, описанной в массиве строк indexes.
 * </p>
 * <p>Каждая строка из массива indexes представляет из себя последовательность чисел, перечисленных через дефис и(или)
 * через запятую. К примеру, запись <code>1-5,7,9-11</code> является последовательностью следующих чисел: <code>1,2,
 * 3,4,5,7,9,10,11</code>.</p>
 */
public interface Port {


    /**
     * <p>Метод, преобразовывающий массив строк indexes в массив последовательностей чисел</p>
     * <p>Массив строк <code>{"1,3-5", "2", "3-4"}</code> преобразуется в следующий массив чисел:
     * <code>{[1, 3, 4, 5], [2], [3, 4]}</code></p>
     *
     * @param indexes array of strings in format <code>{"1,3-5", "2", "3-4"}</code>
     * @return two-dimensional array of integers <code>{[1, 3, 4, 5], [2], [3, 4]}</code>
     * @throws PortException when <code>indexes</code> has wrong format
     */
    int[][] parseIndexes(String[] indexes) throws PortException;

    /**
     * <p>Метод, возвращающий всевозможные уникальные упорядоченные группы элементов полученных массивов чисел./p>
     * <p>Массив строк <code>{"1,3-5", "2", "3-4"}</code> преобразуется в следующий массив чисел:
     * <code>{[1, 2, 3], [1, 2, 4], [3, 2, 3], [3, 2, 4], [4, 2, 3], [4, 2, 4], [5, 2, 3], [5, 2, 4]}</code></p>
     *
     * @param indexes array of strings in format <code>{"1,3-5", "2", "3-4"}</code>
     * @return two-dimensional array of all combinations of integers <code>{[1, 2, 3], [1, 2, 4], [3, 2, 3], [3, 2,
     * 4], [4, 2, 3], [4, 2, 4], [5, 2, 3], [5, 2, 4]}</code>
     * @throws PortException when <code>indexes</code> has wrong format
     */
    int[][] uniqueGroups(String[] indexes) throws PortException;
}
