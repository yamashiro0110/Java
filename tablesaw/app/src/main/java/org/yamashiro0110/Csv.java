package org.yamashiro0110;

import lombok.extern.slf4j.Slf4j;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.StreamSupport;

@Slf4j
public class Csv implements Runnable {

    public static void main(final String[] args) {
        final Csv csv = new Csv();
        csv.run();
    }

    @Override
    public void run() {
        try {
            this.readCsv();
            this.selectColumn();
            this.addColumn();
            this.updateColumn();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * csvを読み込む
     *
     * @return {@link Table}
     * @throws IOException
     */
    private Table table() throws IOException {
        return Table.read().csv("app/sample.csv");
    }

    /**
     * 読み込んだcsvを表示
     *
     * @throws IOException
     */
    private void readCsv() throws IOException {
        final Table csv = this.table();
        log.info(csv.print());
    }

    /**
     * 選択したカラムを表示
     *
     * @throws IOException
     */
    private void selectColumn() throws IOException {
        final Table csv = this.table();
        log.info(csv.stringColumn("name").print());
    }

    /**
     * カラムを追加して表示
     *
     * @throws IOException
     */
    private void addColumn() throws IOException {
        final Table csv = this.table();
        final IntColumn column = csv.intColumn("id");
        final IntColumn newColumn = IntColumn.create("newId");

        StreamSupport.stream(column.spliterator(), false)
                .forEach(value -> newColumn.append(value + 10));
        csv.addColumns(newColumn);
        log.info(csv.print());
    }

    /**
     * カラムを更新して表示
     *
     * @throws IOException
     */
    private void updateColumn() throws IOException {
        final Table csv = this.table();
        final StringColumn newColumn = csv.stringColumn("name_en").upperCase().setName("name_en");
        csv.removeColumns("name_en").addColumns(newColumn);
        log.info("updateColumn {}", csv.print());
    }

}
