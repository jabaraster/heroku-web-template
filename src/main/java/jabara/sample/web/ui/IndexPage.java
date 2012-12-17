/**
 * 
 */
package jabara.sample.web.ui;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * @author jabaraster
 */
public class IndexPage extends WebPage {
    private static final long serialVersionUID = -3725581870632049658L;

    private final AtomicLong  counterModel     = new AtomicLong(0);

    private Label             label;
    private Form<?>           form;
    private AjaxButton        updater;

    /**
     * 
     */
    public IndexPage() {
        this.add(getLabel());
        this.add(getForm());
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<Object>("form"); //$NON-NLS-1$
            this.form.add(getUpdater());
        }
        return this.form;
    }

    private Label getLabel() {
        if (this.label == null) {
            this.label = new Label("label", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
                        private static final long serialVersionUID = -7805571669898825029L;

                        @SuppressWarnings("synthetic-access")
                        @Override
                        public String getObject() {
                            try {
                                TimeUnit.MILLISECONDS.sleep(200);
                            } catch (final InterruptedException e) {
                                // 処理なし
                            }

                            return String.valueOf(IndexPage.this.counterModel.incrementAndGet());
                        }
                    });
            this.label.setOutputMarkupId(true);
        }
        return this.label;
    }

    private AjaxButton getUpdater() {
        if (this.updater == null) {
            this.updater = new IndicatingAjaxButton("updater") { //$NON-NLS-1$
                private static final long serialVersionUID = -3511656114402532177L;

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    pTarget.add(IndexPage.this.getLabel());
                    pTarget.focusComponent(getUpdater());
                }
            };
        }

        return this.updater;
    }
}
