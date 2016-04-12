package com.booboot.vndbandroid.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vndetails.VNDetailsElement;
import com.booboot.vndbandroid.adapter.vndetails.VNExpandableListAdapter;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Category;
import com.booboot.vndbandroid.api.bean.Genre;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Language;
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.api.bean.Platform;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Relation;
import com.booboot.vndbandroid.api.bean.Screen;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Tag;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.db.DB;
import com.booboot.vndbandroid.listener.VNDetailsListener;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class VNDetailsActivity extends AppCompatActivity {
    public final static String TITLE_INFORMATION = "Information";
    public final static String TITLE_DESCRIPTION = "Description";
    public final static String TITLE_GENRES = "Genres";
    public final static String TITLE_CHARACTERS = "Characters";
    public final static String TITLE_SCREENSHOTS = "Screenshots";
    public final static String TITLE_STATS = "Stats";
    public final static String TITLE_TAGS = "Tags";
    public final static String TITLE_RELATIONS = "Relations";
    public final static String TITLE_PLATFORMS = "Platforms";
    public final static String TITLE_LANGUAGES = "Languages";

    private ActionBar actionBar;
    private Item vn;
    private Item vnlistVn;
    private Item wishlistVn;
    private Item votelistVn;
    private List<Item> characters;

    private ImageButton image;
    private Button statusButton;
    private Button wishlistButton;
    private Button votesButton;

    private VNDetailsListener listener;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private VNDetailsElement characterElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(VNTypeFragment.VN_ARG);
        listener = new VNDetailsListener(this, vn);

        initCharacters();
        init();
    }

    private void init() {
        vnlistVn = DB.vnlist.get(vn.getId());
        wishlistVn = DB.wishlist.get(vn.getId());
        votelistVn = DB.votelist.get(vn.getId());
        if (DB.characters.get(vn.getId()) != null) {
            characters = DB.characters.get(vn.getId());
        }

        initExpandableListView();

        image = (ImageButton) findViewById(R.id.image);
        statusButton = (Button) findViewById(R.id.statusButton);
        wishlistButton = (Button) findViewById(R.id.wishlistButton);
        votesButton = (Button) findViewById(R.id.votesButton);

        actionBar = getSupportActionBar();
        actionBar.setTitle(vn.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        statusButton.setText(Status.toString(vnlistVn != null ? vnlistVn.getStatus() : -1));
        wishlistButton.setText(Priority.toString(wishlistVn != null ? wishlistVn.getPriority() : -1));
        votesButton.setText(Vote.toString(votelistVn != null ? votelistVn.getVote() : -1));

        ImageLoader.getInstance().displayImage(vn.getImage(), image);
        Lightbox.set(VNDetailsActivity.this, image, vn.getImage());
    }

    private void initExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = getExpandableListData();
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new VNExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.addHeaderView(getLayoutInflater().inflate(R.layout.vn_details_header, null));
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() == R.id.list_item_text_layout) {
                    TextView itemLeftText = (TextView) view.findViewById(R.id.itemLeftText);
                    TextView itemRightText = (TextView) view.findViewById(R.id.itemRightText);

                    String copiedText = itemLeftText.getText().toString();
                    if (!itemRightText.getText().toString().isEmpty())
                        copiedText += " : " + itemRightText.getText().toString();

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("CLIPBOARD", copiedText);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(VNDetailsActivity.this, "Element copied to clipboard.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        /* Disables click on certain elements if they have finished loading */
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.getExpandableListAdapter().getChildrenCount(groupPosition) < 1) {
                    Toast.makeText(VNDetailsActivity.this, "Nothing to show here yet...", Toast.LENGTH_SHORT).show();
                    return true;
                } else
                    return false;
            }
        });
    }

    private void initCharacters() {
        if (DB.characters.get(vn.getId()) == null) {
            VNDBServer.get("character", DB.CHARACTER_FLAGS, "(vn = " + vn.getId() + ")", Options.create(1, 25, null, false), this, new Callback() {
                @Override
                protected void config() {
                    DB.characters.put(vn.getId(), results.getItems());
                    characters = results.getItems();

                    List<String> character_images = new ArrayList<>();
                    List<String> character_names = new ArrayList<>();
                    List<String> character_subnames = new ArrayList<>();
                    for (Item character : characters) {
                        character_images.add(character.getImage());
                        character_names.add(character.getName());
                        character_subnames.add(character.getOriginal());
                    }

                    characterElement.setPrimaryData(character_names);
                    characterElement.setSecondaryData(character_subnames);
                    characterElement.setUrlImages(character_images);
                }
            }, Callback.errorCallback(this));
        }
    }

    public void showStatusPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.status, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        listener.setPopupButton(statusButton);
        popup.show();
    }

    public void showWishlistPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.wishlist, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        listener.setPopupButton(wishlistButton);
        popup.show();
    }

    public void showVotesPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.votes, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        listener.setPopupButton(votesButton);
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public LinkedHashMap<String, VNDetailsElement> getExpandableListData() {
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = new LinkedHashMap<>();

        List<String> infoLeft = new ArrayList<>();
        List<String> infoRight = new ArrayList<>();
        List<Integer> infoRightImages = new ArrayList<>();
        infoLeft.add("Title");
        infoRight.add(vn.getTitle());
        infoRightImages.add(-1);
        if (vn.getOriginal() != null) {
            infoLeft.add("Original title");
            infoRight.add(vn.getOriginal());
            infoRightImages.add(-1);
        }

        try {
            infoLeft.add("Released date");
            Date released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(vn.getReleased());
            infoRight.add(new SimpleDateFormat("d MMMM yyyy", Locale.US).format(released));
            infoRightImages.add(-1);
        } catch (ParseException e) {
        }

        if (vn.getAliases() != null) {
            infoLeft.add("Aliases");
            infoRight.add(vn.getAliases().replace("\n", "<br>"));
            infoRightImages.add(-1);
        }

        infoLeft.add("Length");
        infoRight.add(vn.getLengthString());
        infoRightImages.add(vn.getLengthImage());

        infoLeft.add("Links");
        Links links = vn.getLinks();
        String htmlLinks = "";
        if (links.getWikipedia() != null) htmlLinks += "<a href=\"" + Links.WIKIPEDIA + links.getWikipedia() + "\">Wikipedia</a>";
        if (links.getEncubed() != null) htmlLinks += "<br><a href=\"" + Links.ENCUBED + links.getEncubed() + "\">Encubed</a>";
        if (links.getRenai() != null) htmlLinks += "<br><a href=\"" + Links.RENAI + links.getRenai() + "\">Renai</a>";
        infoRight.add(htmlLinks);
        infoRightImages.add(-1);

        List<String> description = new ArrayList<>();
        description.add(vn.getDescription());

        List<String> genres = new ArrayList<>();
        for (List<Number> tag : vn.getTags()) {
            String tagName = Tag.getTags(this).get(tag.get(0).intValue()).getName();
            if (Genre.contains(tagName)) {
                genres.add(tagName);
            }
        }

        if (characters == null) {
            characterElement = new VNDetailsElement(null, new ArrayList<String>(), null, null, null, VNDetailsElement.TYPE_SUBTITLE);
        } else {
            List<String> character_images = new ArrayList<>();
            List<String> character_names = new ArrayList<>();
            List<String> character_subnames = new ArrayList<>();
            for (Item character : characters) {
                character_images.add(character.getImage());
                character_names.add(character.getName());
                character_subnames.add(character.getOriginal());
            }
            characterElement = new VNDetailsElement(null, character_names, character_subnames, null, character_images, VNDetailsElement.TYPE_SUBTITLE);
        }

        List<String> tags = new ArrayList<>();
        List<Integer> tags_images = new ArrayList<>();
        List<String> alreadyProcessedTags = new ArrayList<>();
        for (List<Number> cat : vn.getTags()) {
            String currentCategory = Tag.getTags(this).get(cat.get(0).intValue()).getCat();
            if (!alreadyProcessedTags.contains(currentCategory)) {
                alreadyProcessedTags.add(currentCategory);
                tags.add("<b>" + Category.CATEGORIES.get(currentCategory) + " :</b>");
                tags_images.add(-1);
                for (List<Number> tag : vn.getTags()) {
                    String tagCat = Tag.getTags(this).get(tag.get(0).intValue()).getCat();
                    if (tagCat.equals(currentCategory)) {
                        String tagName = Tag.getTags(this).get(tag.get(0).intValue()).getName();
                        tags.add(tagName);
                        tags_images.add(Tag.getScoreImage(tag));
                    }
                }
            }
        }

        List<Integer> relation_ids = new ArrayList<>();
        List<String> relation_titles = new ArrayList<>();
        List<String> relation_types = new ArrayList<>();
        for (Relation relation : vn.getRelations()) {
            relation_titles.add(relation.getTitle());
            relation_types.add(Relation.TYPES.get(relation.getRelation()));
            relation_ids.add(relation.getId());
        }

        List<String> screenshots = new ArrayList<>();
        for (Screen screenshot : vn.getScreens()) {
            screenshots.add(screenshot.getImage());
        }

        List<String> statLeft = new ArrayList<>();
        List<String> statRight = new ArrayList<>();
        List<Integer> statRightImages = new ArrayList<>();
        statLeft.add("Popularity");
        statRight.add(vn.getPopularity() + "%");
        statRightImages.add(vn.getPopularityImage());
        statLeft.add("Rating");
        statRight.add(vn.getRating() + " (" + Vote.getName(vn.getRating()) + ")<br>" + vn.getVotecount() + " votes total");
        statRightImages.add(vn.getRatingImage());

        List<String> platforms = new ArrayList<>();
        List<Integer> platforms_images = new ArrayList<>();
        for (String platform : vn.getPlatforms()) {
            platforms.add(Platform.FULL_TEXT.get(platform));
            platforms_images.add(Platform.IMAGES.get(platform));
        }

        List<String> languages = new ArrayList<>();
        List<Integer> languages_flags = new ArrayList<>();
        for (String language : vn.getLanguages()) {
            languages.add(Language.FULL_TEXT.get(language));
            languages_flags.add(Language.FLAGS.get(language));
        }

        expandableListDetail.put(TITLE_INFORMATION, new VNDetailsElement(null, infoLeft, infoRight, infoRightImages, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_DESCRIPTION, new VNDetailsElement(null, description, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_GENRES, new VNDetailsElement(null, genres, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_CHARACTERS, characterElement);
        expandableListDetail.put(TITLE_SCREENSHOTS, new VNDetailsElement(null, screenshots, null, null, null, VNDetailsElement.TYPE_IMAGES));
        expandableListDetail.put(TITLE_STATS, new VNDetailsElement(null, statLeft, statRight, statRightImages, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_TAGS, new VNDetailsElement(tags_images, tags, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_RELATIONS, new VNDetailsElement(relation_ids, relation_titles, relation_types, null, null, VNDetailsElement.TYPE_SUBTITLE));
        expandableListDetail.put(TITLE_PLATFORMS, new VNDetailsElement(platforms_images, platforms, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_LANGUAGES, new VNDetailsElement(languages_flags, languages, null, null, null, VNDetailsElement.TYPE_TEXT));

        return expandableListDetail;
    }

    public List<Item> getCharacters() {
        return characters;
    }
}
