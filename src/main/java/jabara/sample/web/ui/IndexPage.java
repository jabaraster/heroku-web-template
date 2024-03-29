/**
 * 
 */
package jabara.sample.web.ui;

import jabara.sample.entity.ESample;
import jabara.sample.entity.ESample_;
import jabara.sample.service.SampleService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.inject.Inject;

/**
 * WicketのPageクラスのサンプルです. <br>
 * PageクラスのオブジェクトはHttpSessionに保存されます. <br>
 * つまりPageクラスのオブジェクトはステートフル(状態を持つ)です. <br>
 * 
 * @author jabaraster
 */
public class IndexPage extends WebPage {
    private static final long   serialVersionUID = -3725581870632049658L;

    /**
     * ビジネスロジックは{@link Inject}アノテーションを付与しておけば、Guiceが自動的に適切なオブジェクトをセットしてくれます. <br>
     * これは、SampleWicketApplicationクラスでの仕込みが効いています. <br>
     */
    @Inject
    SampleService               sampleService;

    /**
     * counterという名前のLabelは、このオブジェクトを値として持ちます. <br>
     * ただしcounterが直接counterValueを参照するのではなく、間にModelがいます. <br>
     * つまりModelがコンポーネントと値を結んでいるわけです. <br>
     * この構造は高い柔軟性をもたらします. <br>
     * コンポーネントは値がどこから来たかを意識する必要がなくなるため、同じコンポーネントがいろんな値を扱うことが出来るようになるためです. <br>
     * なおModelを表すのは{@link IModel}インターフェイスです. <br>
     */
    private final AtomicLong    counterValue     = new AtomicLong(0);

    /**
     * 集合を扱うときは、値としてListを使うのが一般的です.
     */
    private final List<ESample> samplesValue     = new ArrayList<ESample>();

    private Label               label;

    private Form<?>             form;
    private AjaxButton          updater;

    private Form<?>             insertForm;
    private Label               sampleCount;

    /** エラーメッセージを含む、メッセージを表示するためのコンポーネントです. */
    private FeedbackPanel       feedback;

    /** Long型を入力するためのテキストフィールドです. */
    private TextField<Long>     code;
    private TextField<String>   name;
    private AjaxButton          inserter;

    /**
     * 繰り返し項目を扱うには、ListViewを使います. <br>
     * ただ、ListViewはページングに対応していません. <br>
     * ページングを扱うには{@link PageableListView}、あるいは{@link DataTable}を使います. <br>
     * 前者は表示対象のデータが全てメモリ上に乗る場合に使用します(乗らない場合も頑張れば使えますが、DataTableを使った方が手っ取り早い). <br>
     * 後者はページに表示するデータをつどDBから取得して表示する場合に使用します. <br>
     */
    private ListView<ESample>   samples;

    /**
     * 繰り返し項目をAjaxで更新する場合、親コンポーネントが必要です. <br>
     * これはWicketの制約でありルールです. <br>
     * このように「業務的な意味はないがどうしても作らないといけない」コンポーネントとして、WicketはWebMarkupContainerを提供しています. <br>
     */
    private WebMarkupContainer  samplesContainer;

    /**
     * 
     */
    public IndexPage() {

        // コンポーネントの入れ子構造を作成します.
        // つまりPageクラスを頂点としてツリー構造になります.
        // この構造はVBのGUIと同じ構造です.
        // Wicketの場合、Javaオブジェクトのツリー構造がHTMLと同期していなければなりません.
        // つまり、HTMLのwicket:id属性の付いたタグの入れ子構造が、ここで構築しているコンポーネントツリーと一致していなければなりません.
        // 一致していない場合、HTMLが描画されずエラーとなります.
        this.add(getLabel());
        this.add(getForm());
        this.add(getSampleCount());
        this.add(getInsertForm());
        this.add(getSamplesContainer());

        // 繰り返し項目の値を初期化します.
        // ここでは単純にDBから全件を取得していますが、件数が多くなってきた場合はページングの採用を検討するべきです.
        this.samplesValue.addAll(this.sampleService.getAll());
    }

    private TextField<Long> getCode() {
        if (this.code == null) {
            final Model<Long> m = new Model<Long>(); // 値を内包するModelです.

            // HTML5で追加された<input type="number" />に対応したWicketコンポーネントを使います.
            // 大事なのは第３引数で、これを省くと動作しません(これはWicketの設計が悪い).
            // 第３引数には変換した先の数値クラスを指定します.
            this.code = new NumberTextField<Long>("code", m, Long.class); //$NON-NLS-1$

            // 必須入力にします.
            this.code.setRequired(true);
        }
        return this.code;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
            this.feedback.setOutputMarkupId(true); // Ajaxで扱うコンポーネントはこのoutputMarkupIdをtrueにする必要があります. <br>
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<Object>("form"); //$NON-NLS-1$
            this.form.add(getUpdater());
        }
        return this.form;
    }

    @SuppressWarnings({ "serial", "nls" })
    private AjaxButton getInserter() {
        if (this.inserter == null) {
            this.inserter = new IndicatingAjaxButton("inserter") { // IndicatingAjaxButtonはAjax処理中にぐるぐるアニメが表示されるボタン.
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    // ボタンを押したときに入力値にエラーが合った場合、このメソッドが動きます.
                    // Ajax通信しているので、画面全体が再描画されることはありません.
                    // 代わりに、再描画してほしいコンポーネントをWicketに知らせます.
                    // それがAjaxRequestTarget#add()メソッドです.
                    pTarget.add(getFeedback());
                }

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    onInserterClick(pTarget);
                }
            };
        }
        return this.inserter;
    }

    private Form<?> getInsertForm() {
        if (this.insertForm == null) {
            this.insertForm = new Form<Object>("insertForm"); //$NON-NLS-1$
            this.insertForm.add(getFeedback());
            this.insertForm.add(getCode());
            this.insertForm.add(getName());
            this.insertForm.add(getInserter());
        }
        return this.insertForm;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Label getLabel() {
        if (this.label == null) {

            this.label = new Label("label", new AbstractReadOnlyModel<String>() {
                // AbstractReadOnlyModelは、読み取り専用の値をコンポーネントに与えるためのクラスです.
                // コンポーネントが描画されるときに値が必要になり、getObject()メソッドが呼び出されます.
                // getObject()からは、そのときどきで適当なオブジェクトを返すように実装しておけば
                // 良い感じに画面が描画されることになります.

                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    // 画面のぐるぐるをアピールしたいので、少し待ちます.
                    sleep();
                    // カウンターをカウントアップして返します.
                    // これによりボタンを押す度に数値が増加します.
                    return String.valueOf(IndexPage.this.counterValue.incrementAndGet());
                }
            });
            this.label.setOutputMarkupId(true); // Ajaxで利用.
        }
        return this.label;
    }

    private TextField<String> getName() {
        if (this.name == null) {
            this.name = new TextField<String>("name", new Model<String>()); //$NON-NLS-1$
            this.name.setRequired(true);
        }
        return this.name;
    }

    @SuppressWarnings({ "nls", "serial" })
    private Label getSampleCount() {
        if (this.sampleCount == null) {
            this.sampleCount = new Label("sampleCount", new AbstractReadOnlyModel<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    // 値が必要なタイミングで、つどDBのレコード数を数えて返します.
                    return String.valueOf(getSampleCountByDb());
                }
            });
            this.sampleCount.setOutputMarkupId(true); // Ajaxで利用.
        }
        return this.sampleCount;
    }

    private int getSampleCountByDb() {
        return this.sampleService.countAll();
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<ESample> getSamples() {
        if (this.samples == null) {
            this.samples = new ListView<ESample>("samples", Model.ofList(this.samplesValue)) {
                @Override
                protected void populateItem(final ListItem<ESample> pItem) {
                    pItem.setModel(new CompoundPropertyModel<ESample>(pItem.getModelObject()));
                    pItem.add(new Label(ESample_.code.getName()));
                    pItem.add(new Label(ESample_.name.getName()));
                }
            };
        }

        return this.samples;
    }

    @SuppressWarnings("nls")
    private WebMarkupContainer getSamplesContainer() {
        if (this.samplesContainer == null) {
            this.samplesContainer = new WebMarkupContainer("samplesContainer");
            this.samplesContainer.setOutputMarkupId(true);
            this.samplesContainer.add(getSamples());
        }

        return this.samplesContainer;
    }

    @SuppressWarnings("serial")
    private AjaxButton getUpdater() {
        if (this.updater == null) {
            this.updater = new IndicatingAjaxButton("updater") { //$NON-NLS-1$

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    // 再描画するラベルをAjaxRequestTargetに登録します.
                    // getLabel()が返すコンポーネント(label)はModelにAbstractReadOnlyModelをセットしていることを思い出して下さい.
                    // 再描画が必要になると、labelコンポーネントはModelを通して値を取得します.
                    // セットされているAbstractReadOnlyModelは、counterValueをインクリメントしてその値を返すように実装しています.
                    // つまり画面上ではインクリメントされた値に書き換わることになります.
                    pTarget.add(IndexPage.this.getLabel());

                    pTarget.focusComponent(getUpdater()); // レスポンス完了後にフォーカスを当てるコンポーネントを指定.
                }
            };
        }
        return this.updater;
    }

    private void onInserterClick(final AjaxRequestTarget pTarget) {
        this.sampleService.insert(getCode().getModelObject().longValue(), getName().getModelObject());

        pTarget.add(getSampleCount());

        final List<ESample> all = this.sampleService.getAll();
        this.samplesValue.clear();
        this.samplesValue.addAll(all);
        pTarget.add(getSamplesContainer());
    }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (final InterruptedException e) {
            // 処理なし
        }
    }
}
