package pt.simdea.guestlist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

class MaterialSearchView : FrameLayout {

    constructor(context: Context) : super(context) {
        initiateView()
        initStyle(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initiateView()
        initStyle(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initiateView()
        initStyle(attrs, defStyleAttr)
    }

    private var menuItem: MenuItem? = null
    /**
     * Return true if search is open
     *
     * @return
     */
    var isSearchOpen = false
        private set

    private var clearingFocus: Boolean = false

    //Views
    private var searchLayout: View? = null
    private var searchSrcTextView: EditText? = null
    private var backBtn: ImageButton? = null
    private var voiceBtn: ImageButton? = null
    private var emptyBtn: ImageButton? = null
    private var searchTopBar: RelativeLayout? = null

    private var oldQueryText: CharSequence? = null
    private var userQuery: CharSequence? = null

    private var onQueryChangeListener: OnQueryTextListener? = null

    private var savedState: SavedState? = null

    private var allowVoiceSearch: Boolean = false
    private val mOnClickListener = OnClickListener { v ->
        when {
            v === backBtn -> closeSearch()
            v === voiceBtn -> onVoiceClicked()
            v === emptyBtn -> searchSrcTextView!!.text = null
        }
    }

    private val isVoiceAvailable: Boolean
        get() {
            val pm = context.packageManager
            val activities = pm.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
            return activities.size != 0
        }

    private fun initStyle(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, defStyleAttr, 0)

        if (a != null) {
            if (a.hasValue(R.styleable.MaterialSearchView_searchBackground)) {
                background = a.getDrawable(R.styleable.MaterialSearchView_searchBackground)
            }

            if (a.hasValue(R.styleable.MaterialSearchView_android_textColor)) {
                setTextColor(a.getColor(R.styleable.MaterialSearchView_android_textColor, 0))
            }

            if (a.hasValue(R.styleable.MaterialSearchView_android_textColorHint)) {
                setHintTextColor(a.getColor(R.styleable.MaterialSearchView_android_textColorHint, 0))
            }

            if (a.hasValue(R.styleable.MaterialSearchView_android_hint)) {
                setHint(a.getString(R.styleable.MaterialSearchView_android_hint))
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchVoiceIcon)) {
                setVoiceIcon(a.getDrawable(R.styleable.MaterialSearchView_searchVoiceIcon))
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchCloseIcon)) {
                setCloseIcon(a.getDrawable(R.styleable.MaterialSearchView_searchCloseIcon))
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchBackIcon)) {
                setBackIcon(a.getDrawable(R.styleable.MaterialSearchView_searchBackIcon))
            }

            a.recycle()
        }
    }

    private fun initiateView() {
        LayoutInflater.from(context).inflate(R.layout.search_view, this, true)
        searchLayout = findViewById(R.id.search_layout)

        searchTopBar = searchLayout!!.findViewById(R.id.search_top_bar)
        searchSrcTextView = searchLayout!!.findViewById(R.id.searchTextView)
        searchSrcTextView!!.setHintTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        backBtn = searchLayout!!.findViewById(R.id.action_up_btn)
        voiceBtn = searchLayout!!.findViewById(R.id.action_voice_btn)
        emptyBtn = searchLayout!!.findViewById(R.id.action_empty_btn)

        searchSrcTextView!!.setOnClickListener(mOnClickListener)
        backBtn!!.setOnClickListener(mOnClickListener)
        voiceBtn!!.setOnClickListener(mOnClickListener)
        emptyBtn!!.setOnClickListener(mOnClickListener)

        allowVoiceSearch = false

        showVoice(true)

        initSearchView()
    }

    private fun initSearchView() {
        searchSrcTextView!!.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            true
        }

        searchSrcTextView!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                userQuery = s
                this@MaterialSearchView.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        searchSrcTextView!!.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard(searchSrcTextView!!)
            }
        }
    }

    private fun onVoiceClicked() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak an item name or number");    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)    // setting recognition model, optimized for short phrases – search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)    // quantity of results we want to receive
        if (context is Activity) {
            (context as Activity).startActivityForResult(intent, REQUEST_VOICE)
        }
    }

    private fun onTextChanged(newText: CharSequence) {
        val text = searchSrcTextView!!.text
        userQuery = text
        val hasText = !TextUtils.isEmpty(text)
        if (hasText) {
            emptyBtn!!.visibility = View.VISIBLE
            showVoice(false)
        } else {
            emptyBtn!!.visibility = View.GONE
            showVoice(true)
        }

        if (onQueryChangeListener != null && !TextUtils.equals(newText, oldQueryText)) {
            onQueryChangeListener!!.onQueryTextChange(newText.toString())
        }
        oldQueryText = newText.toString()
    }

    private fun onSubmitQuery() {
        val query = searchSrcTextView!!.text
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (onQueryChangeListener == null || !onQueryChangeListener!!.onQueryTextSubmit(query.toString())) {
                closeSearch()
                searchSrcTextView!!.text = null
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showKeyboard(view: View) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus()
        }
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    //Public Attributes

    override fun setBackground(background: Drawable?) {
        searchTopBar!!.background = background
    }

    override fun setBackgroundColor(color: Int) {
        searchTopBar!!.setBackgroundColor(color)
    }

    fun setTextColor(color: Int) {
        searchSrcTextView!!.setTextColor(color)
    }

    fun setHintTextColor(color: Int) {
        searchSrcTextView!!.setHintTextColor(color)
    }

    fun setHint(hint: CharSequence?) {
        searchSrcTextView!!.hint = hint
    }

    fun setVoiceIcon(drawable: Drawable?) {
        voiceBtn!!.setImageDrawable(drawable)
    }

    fun setCloseIcon(drawable: Drawable?) {
        emptyBtn!!.setImageDrawable(drawable)
    }

    fun setBackIcon(drawable: Drawable?) {
        backBtn!!.setImageDrawable(drawable)
    }

    fun setVoiceSearch(voiceSearch: Boolean) {
        allowVoiceSearch = voiceSearch
    }

    //Public Methods

    /**
     * Calling this will set the query to search text box. if submit is true, it'll submit the query.
     *
     * @param query
     * @param submit
     */
    fun setQuery(query: CharSequence?, submit: Boolean) {
        searchSrcTextView!!.setText(query)
        if (query != null) {
            searchSrcTextView!!.setSelection(searchSrcTextView!!.length())
            userQuery = query
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery()
        }
    }

    /**
     * if show is true, this will enable voice search. If voice is not available on the device, this method call has not effect.
     *
     * @param show
     */
    fun showVoice(show: Boolean) {
        if (show && isVoiceAvailable && allowVoiceSearch) {
            voiceBtn!!.visibility = View.VISIBLE
        } else {
            voiceBtn!!.visibility = View.GONE
        }
    }

    /**
     * Call this method and pass the menu item so this class can handle click events for the Menu Item.
     *
     * @param menuItem
     */
    fun setMenuItem(menuItem: MenuItem) {
        this.menuItem = menuItem
        this.menuItem!!.setOnMenuItemClickListener {
            showSearch()
            true
        }
    }

    /**
     * Open Search View. if animate is true, Animate the showing of the view.
     *
     * @param animate
     */
    @JvmOverloads
    fun showSearch(animate: Boolean = true) {
        if (isSearchOpen) {
            return
        }

        //Request Focus
        searchSrcTextView!!.text = null
        searchSrcTextView!!.requestFocus()

        if (animate) {
            AnimationUtil.fadeInView(searchLayout!!, AnimationUtil.ANIMATION_DURATION_MEDIUM, object : AnimationUtil.AnimationListener {
                override fun onAnimationStart(view: View): Boolean {
                    return false
                }

                override fun onAnimationEnd(view: View): Boolean {
                    return false
                }

                override fun onAnimationCancel(view: View): Boolean {
                    return false
                }
            })
        } else {
            searchLayout!!.visibility = View.VISIBLE
        }
        isSearchOpen = true
    }

    /**
     * Close search view.
     */
    fun closeSearch() {
        if (!isSearchOpen) {
            return
        }

        searchSrcTextView!!.text = null
        clearFocus()

        searchLayout!!.visibility = View.GONE
        isSearchOpen = false

    }

    /**
     * Set this listener to listen to Query Change events.
     *
     * @param listener
     */
    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        onQueryChangeListener = listener
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        // Don't accept focus if in the middle of clearing focus
        if (clearingFocus) return false
        // Check if SearchView is focusable.
        return if (!isFocusable) false else searchSrcTextView!!.requestFocus(direction, previouslyFocusedRect)
    }

    override fun clearFocus() {
        clearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        searchSrcTextView!!.clearFocus()
        clearingFocus = false
    }

    public override fun onSaveInstanceState(): Parcelable? {
        //begin boilerplate code that allows parent classes to save state
        val superState = super.onSaveInstanceState()

        savedState = SavedState(superState!!)
        //end
        savedState!!.query = if (userQuery != null) userQuery!!.toString() else null
        savedState!!.isSearchOpen = this.isSearchOpen

        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        //begin boilerplate code so parent classes can restore state
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        savedState = state

        if (savedState!!.isSearchOpen) {
            showSearch(false)
            setQuery(savedState!!.query, false)
        }

        super.onRestoreInstanceState(savedState!!.superState)
    }

    interface OnQueryTextListener {

        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
         * SearchView perform the default action.
         */
        fun onQueryTextSubmit(query: String): Boolean

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        fun onQueryTextChange(newText: String): Boolean
    }

    internal class SavedState : View.BaseSavedState {
        var query: String? = null
        var isSearchOpen: Boolean = false

        constructor(superState: Parcelable) : super(superState) {}

        private constructor(`in`: Parcel) : super(`in`) {
            this.query = `in`.readString()
            this.isSearchOpen = `in`.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(query)
            out.writeInt(if (isSearchOpen) 1 else 0)
        }


        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object {
        const val REQUEST_VOICE = 9999
    }

}
/**
 * Open Search View. This will animate the showing of the view.
 */