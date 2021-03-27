package org.schabi.newpipe.extractor.services.youtube.stream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.downloader.DownloaderFactory;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.MetaInfo;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ContentNotAvailableException;
import org.schabi.newpipe.extractor.exceptions.GeographicRestrictionException;
import org.schabi.newpipe.extractor.exceptions.PaidContentException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.PrivateContentException;
import org.schabi.newpipe.extractor.exceptions.YoutubeMusicPremiumContentException;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.services.DefaultStreamExtractorTest;
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper;
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor;
import org.schabi.newpipe.extractor.stream.Description;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamSegment;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.extractor.stream.TrackMetadata;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.schabi.newpipe.extractor.ServiceList.YouTube;
import static org.schabi.newpipe.extractor.utils.Utils.EMPTY_STRING;

/*
 * Created by Christian Schabesberger on 30.12.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * YoutubeVideoExtractorDefault.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */
public class YoutubeStreamExtractorDefaultTest {
    private static final String RESOURCE_PATH = DownloaderFactory.RESOURCE_PATH + "services/youtube/extractor/stream/";
    static final String BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_LICENCE = "YouTube licence";

    public static class NotAvailable {
        @BeforeClass
        public static void setUp() throws IOException {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "notAvailable"));
        }

        @Test(expected = GeographicRestrictionException.class)
        public void geoRestrictedContent() throws Exception {
            final StreamExtractor extractor =
                    YouTube.getStreamExtractor(BASE_URL + "_PL2HJKxnOM");
            extractor.fetchPage();
        }

        @Test(expected = ContentNotAvailableException.class)
        public void nonExistentFetch() throws Exception {
            final StreamExtractor extractor =
                    YouTube.getStreamExtractor(BASE_URL + "don-t-exist");
            extractor.fetchPage();
        }

        @Test(expected = ParsingException.class)
        public void invalidId() throws Exception {
            final StreamExtractor extractor =
                    YouTube.getStreamExtractor(BASE_URL + "INVALID_ID_INVALID_ID");
            extractor.fetchPage();
        }

        @Test(expected = PaidContentException.class)
        public void paidContent() throws Exception {
            final StreamExtractor extractor =
                    YouTube.getStreamExtractor(BASE_URL + "ayI2iBwGdxw");
            extractor.fetchPage();
        }

        @Test(expected = PrivateContentException.class)
        public void privateContent() throws Exception {
            final StreamExtractor extractor =
                    YouTube.getStreamExtractor(BASE_URL + "8VajtrESJzA");
            extractor.fetchPage();
        }

        @Test(expected = YoutubeMusicPremiumContentException.class)
        public void youtubeMusicPremiumContent() throws Exception {
            final StreamExtractor extractor =
                    YouTube.getStreamExtractor(BASE_URL + "sMJ8bRN2dak");
            extractor.fetchPage();
        }
    }

    public static class DescriptionTestPewdiepie extends DefaultStreamExtractorTest {
        private static final String ID = "7PIMiDcwNvc";
        private static final int TIMESTAMP = 17;
        private static final String URL = BASE_URL + ID + "&t=" + TIMESTAMP;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "pewdiwpie"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "Marzia & Felix - Wedding 19.08.2019"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "PewDiePie"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UC-lHJZR3Gqxm24_Vd_AJ5Yw"; }
        @Override public List<String> expectedDescriptionContains() {
            return Arrays.asList("https://www.youtube.com/channel/UC7l23W7gFi4Uho6WSzckZRA",
                    "https://www.handcraftpictures.com/");
        }
        @Override public boolean expectedUploaderVerified() { return true; }
        @Override public long expectedLength() { return 381; }
        @Override public long expectedTimestamp() { return TIMESTAMP; }
        @Override public long expectedViewCountAtLeast() { return 26682500; }
        @Nullable @Override public String expectedUploadDate() { return "2019-08-24 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2019-08-24"; }
        @Override public long expectedLikeCountAtLeast() { return 5212900; }
        @Override public long expectedDislikeCountAtLeast() { return 30600; }
        @Override public int expectedStreamSegmentsCount() { return 0; }
        @Override public String expectedLicence() { return YOUTUBE_LICENCE; }
        @Override public String expectedCategory() { return "Entertainment"; }
        // @formatter:on
    }

    public static class DescriptionTestUnboxing extends DefaultStreamExtractorTest {
        private static final String ID = "cV5TjZCJkuA";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "unboxing"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "This Smartphone Changes Everything..."; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return URL; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "Unbox Therapy"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCsTcErHg8oDvUnTzoqsYeNw"; }
        @Override public List<String> expectedDescriptionContains() {
            return Arrays.asList("https://www.youtube.com/watch?v=X7FLCHVXpsA&list=PL7u4lWXQ3wfI_7PgX0C-VTiwLeu0S4v34",
                    "https://www.youtube.com/watch?v=Lqv6G0pDNnw&list=PL7u4lWXQ3wfI_7PgX0C-VTiwLeu0S4v34",
                    "https://www.youtube.com/watch?v=XxaRBPyrnBU&list=PL7u4lWXQ3wfI_7PgX0C-VTiwLeu0S4v34",
                    "https://www.youtube.com/watch?v=U-9tUEOFKNU&list=PL7u4lWXQ3wfI_7PgX0C-VTiwLeu0S4v34");
        }
        @Override public long expectedLength() { return 434; }
        @Override public long expectedViewCountAtLeast() { return 21229200; }
        @Nullable @Override public String expectedUploadDate() { return "2018-06-19 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2018-06-19"; }
        @Override public long expectedLikeCountAtLeast() { return 340100; }
        @Override public long expectedDislikeCountAtLeast() { return 18700; }
        @Override public boolean expectedUploaderVerified() { return true; }
        @Override public String expectedLicence() { return YOUTUBE_LICENCE; }
        @Override public String expectedCategory() { return "Science & Technology"; }
        @Override public List<String> expectedTags() {
            return Arrays.asList("2018", "8 plus", "apple", "apple iphone", "apple iphone x", "best", "best android",
                    "best smartphone", "cool gadgets", "find", "find x", "find x review", "find x unboxing", "findx",
                    "galaxy s9", "galaxy s9+", "hands on", "iphone 8", "iphone 8 plus", "iphone x", "new iphone", "nex",
                    "oneplus 6", "oppo", "oppo find x", "oppo find x hands on", "oppo find x review",
                    "oppo find x unboxing", "oppo findx", "pixel 2 xl", "review", "samsung", "samsung galaxy",
                    "samsung galaxy s9", "smartphone", "unbox therapy", "unboxing", "vivo", "vivo apex", "vivo nex");
        }
        // @formatter:on
    }

    @Ignore("Test broken, video was made private")
    public static class RatingsDisabledTest extends DefaultStreamExtractorTest {
        private static final String ID = "HRKu0cvrr_o";
        private static final int TIMESTAMP = 17;
        private static final String URL = BASE_URL + ID + "&t=" + TIMESTAMP;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "ratingsDisabled"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "AlphaOmegaSin Fanboy Logic: Likes/Dislikes Disabled = Point Invalid Lol wtf?"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "YouTuber PrinceOfFALLEN"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCQT2yul0lr6Ie9qNQNmw-sg"; }
        @Override public List<String> expectedDescriptionContains() { return Arrays.asList("dislikes", "Alpha", "wrong"); }
        @Override public long expectedLength() { return 84; }
        @Override public long expectedTimestamp() { return TIMESTAMP; }
        @Override public long expectedViewCountAtLeast() { return 190; }
        @Nullable @Override public String expectedUploadDate() { return "2019-01-02 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2019-01-02"; }
        @Override public long expectedLikeCountAtLeast() { return -1; }
        @Override public long expectedDislikeCountAtLeast() { return -1; }
        // @formatter:on
    }

    public static class StreamSegmentsTestOstCollection extends DefaultStreamExtractorTest {
        // StreamSegment example with single macro-makers panel
        private static final String ID = "2RYrHwnLHw0";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "streamSegmentsOstCollection"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "1 Hour - Most Epic Anime Mix - Battle Anime OST"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "MathCaires"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UChFoHg6IT18SCqiwCp_KY7Q"; }
        @Override public List<String> expectedDescriptionContains() {
            return Arrays.asList("soundtracks", "9:49", "YouSeeBIGGIRLTT");
        }
        @Override public long expectedLength() { return 3889; }
        @Override public long expectedViewCountAtLeast() { return 2463261; }
        @Nullable @Override public String expectedUploadDate() { return "2019-06-26 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2019-06-26"; }
        @Override public long expectedLikeCountAtLeast() { return 32100; }
        @Override public long expectedDislikeCountAtLeast() { return 750; }
        @Override public boolean expectedHasSubtitles() { return false; }
        @Override public int expectedStreamSegmentsCount() { return 17; }
        @Override public String expectedLicence() { return YOUTUBE_LICENCE; }
        @Override public String expectedCategory() { return "Music"; }
        @Override public List<String> expectedTags() {
            return Arrays.asList("2019", "2019 anime", "Anime OST", "Epic anime ost", "OST Anime",
                    "anime epic soundtrack", "armin", "attack on titan", "battle anime ost", "battle anime soundtracks",
                    "combat anime ost", "epic soundtrack", "eren", "mikasa", "motivational anime ost",
                    "motivational anime soundtracks", "shingeki no kyojin");
        }
        // @formatter:on

        @Test
        public void testStreamSegment() throws Exception {
            final StreamSegment segment = extractor.getStreamSegments().get(3);
            assertEquals(589, segment.getStartTimeSeconds());
            assertEquals("Attack on Titan S2 - YouSeeBIGGIRLTT", segment.getTitle());
            assertEquals(BASE_URL + ID + "?t=589", segment.getUrl());
            assertNotNull(segment.getPreviewUrl());
        }
    }

    public static class StreamSegmentsTestMaiLab extends DefaultStreamExtractorTest {
        // StreamSegment example with macro-makers panel and transcription panel
        private static final String ID = "ud9d5cMDP_0";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "streamSegmentsMaiLab"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "Vitamin D wissenschaftlich gepr\u00fcft"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "maiLab"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCyHDQ5C6z1NDmJ4g6SerW8g"; }
        @Override public List<String> expectedDescriptionContains()  {return Arrays.asList("Vitamin", "2:44", "Was ist Vitamin D?");}
        @Override public boolean expectedUploaderVerified() { return true; }
        @Override public long expectedLength() { return 1010; }
        @Override public long expectedViewCountAtLeast() { return 815500; }
        @Nullable @Override public String expectedUploadDate() { return "2020-11-18 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2020-11-18"; }
        @Override public long expectedLikeCountAtLeast() { return 48500; }
        @Override public long expectedDislikeCountAtLeast() { return 20000; }
        @Override public boolean expectedHasSubtitles() { return true; }
        @Override public int expectedStreamSegmentsCount() { return 7; }
        @Override public String expectedLicence() { return YOUTUBE_LICENCE; }
        @Override public String expectedCategory() { return "Science & Technology"; }
        @Override public List<String> expectedTags() {
            return Arrays.asList("Diabetes", "Erkältung", "Gesundheit", "Immunabwehr", "Immunsystem", "Infektion",
                    "Komisch alles chemisch", "Krebs", "Lab", "Lesch", "Mai", "Mai Thi", "Mai Thi Nguyen-Kim",
                    "Mangel", "Nahrungsergänzungsmittel", "Nguyen", "Nguyen Kim", "Nguyen-Kim", "Quarks", "Sommer",
                    "Supplemente", "Supplements", "Tabletten", "Terra X", "TerraX", "The Secret Life Of Scientists",
                    "Tropfen", "Vitamin D", "Vitamin-D-Mangel", "Vitamine", "Winter", "einnehmen", "maiLab", "nehmen",
                    "supplementieren", "Überdosis", "Überschuss");
        }
        // @formatter:on

        @Test
        public void testStreamSegment() throws Exception {
            final StreamSegment segment = extractor.getStreamSegments().get(1);
            assertEquals(164, segment.getStartTimeSeconds());
            assertEquals("Was ist Vitamin D?", segment.getTitle());
            assertEquals(BASE_URL + ID + "?t=164", segment.getUrl());
            assertNotNull(segment.getPreviewUrl());
        }

        @Override
        @Test
        @Ignore("encoding problem")
        public void testName() {}

        @Override
        @Test
        @Ignore("encoding problem")
        public void testTags() {}
    }

    public static class PublicBroadcasterTest extends DefaultStreamExtractorTest {
        private static final String ID = "q6fgbYWsMgw";
        private static final int TIMESTAMP = 0;
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "publicBroadcast"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "Was verbirgt sich am tiefsten Punkt des Ozeans?"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "Dinge Erklärt – Kurzgesagt"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCwRH985XgMYXQ6NxXDo8npw"; }
        @Override public List<String> expectedDescriptionContains() { return Arrays.asList("Lasst uns abtauchen!", "Angebot von funk", "Dinge"); }
        @Override public long expectedLength() { return 631; }
        @Override public long expectedTimestamp() { return TIMESTAMP; }
        @Override public long expectedViewCountAtLeast() { return 1_600_000; }
        @Nullable @Override public String expectedUploadDate() { return "2019-06-12 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2019-06-12"; }
        @Override public long expectedLikeCountAtLeast() { return 70000; }
        @Override public long expectedDislikeCountAtLeast() { return 500; }
        @Override public List<MetaInfo> expectedMetaInfo() throws MalformedURLException {
            return Collections.singletonList(new MetaInfo(
                    EMPTY_STRING,
                    new Description("Funk is a German public broadcast service.", Description.PLAIN_TEXT),
                    Collections.singletonList(new URL("https://de.wikipedia.org/wiki/Funk_(Medienangebot)?wprov=yicw1")),
                    Collections.singletonList("Wikipedia (German)")
            ));
        }
        @Override public boolean expectedUploaderVerified() { return true; }
        @Override public String expectedLicence() { return YOUTUBE_LICENCE; }
        @Override public String expectedCategory() { return "Education"; }
        @Override public List<String> expectedTags() {
            return Arrays.asList("Abgrund", "Algen", "Bakterien", "Challengertief", "Dumbooktopus",
                    "Dunkel", "Dunkelheit", "Fische", "Flohkrebs", "Hadal-Zone", "Kontinentalschelf",
                    "Licht", "Mariannengraben", "Meer", "Meeresbewohner", "Meeresschnee", "Mesopelagial",
                    "Ozean", "Photosynthese", "Plankton", "Plastik", "Polypen", "Pottwale",
                    "Staatsquelle", "Tauchen", "Tauchgang", "Tentakel", "Tiefe", "Tiefsee", "Tintenfische",
                    "Titanic", "Vampirtintenfisch", "Verschmutzung", "Viperfisch", "Wale");
        }
        // @formatter:on
    }

    public static class YTMusicTrackMetadataTest extends DefaultStreamExtractorTest {
        private static final String ID = "nEipxSHZEIs";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "ytMusicMetadata"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "The Love Club"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "Lorde"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCOxhwqKKlVq_NaD0LVffGuw"; }
        @Override public List<String> expectedDescriptionContains() {
            return Arrays.asList("Provided to YouTube by Universal Music Group",
                    "Personnel, Mixer: Joel Little");
        }
        @Override public boolean expectedUploaderVerified() { return true; }
        @Override public long expectedLength() { return 201; }
        @Override public long expectedViewCountAtLeast() { return 1576672; }
        @Nullable @Override public String expectedUploadDate() { return "2018-07-25 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2018-07-25"; }
        @Override public long expectedLikeCountAtLeast() { return 25986; }
        @Override public long expectedDislikeCountAtLeast() { return 3260; }
        @Override public int expectedStreamSegmentsCount() { return 0; }
        @Override public boolean expectedHasMusicInfos() { return true; }
        @Override public List<TrackMetadata> expectedMusicInfos() {
            return Arrays.asList(
                    new TrackMetadata("The Love Club", "Lorde", "Pure Heroine",
                            new DateWrapper(LocalDate.parse("2013-01-01").atStartOfDay().atOffset(ZoneOffset.UTC)))
            );
        }
        @Override public boolean expectedHasSubtitles() { return false; }
        // @formatter:on
    }

    public static class MusicInVideoMetadataTestSingle extends DefaultStreamExtractorTest {
        private static final String ID = "JQGRg8XBnB4";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "musicInVideoMetadataSingle"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "[MV] MOMOLAND (모모랜드) _ BBoom BBoom (뿜뿜)"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "1theK (원더케이)"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCweOkPb1wVVH0Q0Tlj4a5Pw"; }
        @Override public List<String> expectedDescriptionContains() {
            return Arrays.asList("The best rookie MOMOLAND",
                    "English subtitles are now available");
        }
        @Override public boolean expectedUploaderVerified() { return true; }
        @Override public long expectedLength() { return 210; }
        @Override public long expectedViewCountAtLeast() { return 494455598; }
        @Nullable @Override public String expectedUploadDate() { return "2018-01-03 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2018-01-03"; }
        @Override public long expectedLikeCountAtLeast() { return 3700000; }
        @Override public long expectedDislikeCountAtLeast() { return 291832; }
        @Override public int expectedStreamSegmentsCount() { return 0; }
        @Override public boolean expectedHasMusicInfos() { return true; }
        @Override public List<TrackMetadata> expectedMusicInfos() {
            TrackMetadata trackMetadata = new TrackMetadata();
            trackMetadata.setTitle("BBoom BBoom(뿜뿜) inst");
            trackMetadata.setArtist("MOMOLAND(모모랜드)");
            trackMetadata.setAlbum("GREAT!");
            return Arrays.asList(trackMetadata);
        }
        @Override public boolean expectedHasSubtitles() { return true; }
        // @formatter:on
    }

    public static class MusicInVideoMetadataTestMultiple extends DefaultStreamExtractorTest {
        private static final String ID = "Sqn5-zXUCps";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            YoutubeParsingHelper.resetClientVersionAndKey();
            NewPipe.init(new DownloaderFactory().getDownloader(RESOURCE_PATH + "musicInVideoMetadataMultiple"));
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        // @formatter:off
        @Override public StreamExtractor extractor() { return extractor; }
        @Override public StreamingService expectedService() { return YouTube; }
        @Override public String expectedName() { return "wistful"; }
        @Override public String expectedId() { return ID; }
        @Override public String expectedUrlContains() { return BASE_URL + ID; }
        @Override public String expectedOriginalUrlContains() { return URL; }

        @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
        @Override public String expectedUploaderName() { return "Jo-ha-Q"; }
        @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCrI--yLLfrN8TPNVBRbW-bA"; }
        @Override public List<String> expectedDescriptionContains() {
            return Arrays.asList("Art by Yoshiyuki Sadamoto");
        }
        @Override public boolean expectedUploaderVerified() { return false; }
        @Override public long expectedLength() { return 2865; }
        @Override public long expectedViewCountAtLeast() { return 15083; }
        @Nullable @Override public String expectedUploadDate() { return "2019-12-06 00:00:00.000"; }
        @Nullable @Override public String expectedTextualUploadDate() { return "2019-12-06"; }
        @Override public long expectedLikeCountAtLeast() { return 1000; }
        @Override public long expectedDislikeCountAtLeast() { return 0; }
        @Override public int expectedStreamSegmentsCount() { return 0; }
        @Override public boolean expectedHasMusicInfos() { return true; }
        @Override public List<TrackMetadata> expectedMusicInfos() {
            return Arrays.asList(
                    new TrackMetadata("03 幻の夢", "斉藤和義", "素敵な匂いの世界", null),
                    new TrackMetadata("Love Was Really Gone (2018 Remaster)", "Makoto Matsushita", "Love Was Really Gone", null),
                    new TrackMetadata("Half Moon", null, null, null),
                    new TrackMetadata("M11 re-arrange and re-mix", "ARIANNE", "Shiro SAGISU outtakes from Evangelion", null),
                    new TrackMetadata("Kuki -Stem-", "Sheena Ringo, Nako Saito", null, null)
            );
        }
        @Override public boolean expectedHasSubtitles() { return false; }
        // @formatter:on
    }

    public static class UnlistedTest {
        private static YoutubeStreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            NewPipe.init(DownloaderTestImpl.getInstance());
            extractor = (YoutubeStreamExtractor) YouTube
                    .getStreamExtractor("https://www.youtube.com/watch?v=tjz2u2DiveM");
            extractor.fetchPage();
        }

        @Test
        public void testGetUnlisted() {
            assertEquals(StreamExtractor.Privacy.UNLISTED, extractor.getPrivacy());
        }
    }

    public static class CCLicensed {
        private static final String ID = "M4gD1WSo5mA";
        private static final String URL = BASE_URL + ID;
        private static StreamExtractor extractor;

        @BeforeClass
        public static void setUp() throws Exception {
            NewPipe.init(DownloaderTestImpl.getInstance());
            extractor = YouTube.getStreamExtractor(URL);
            extractor.fetchPage();
        }

        @Test
        public void testGetLicence() throws ParsingException {
            assertEquals("Creative Commons Attribution licence (reuse allowed)", extractor.getLicence());
        }
    }
}
