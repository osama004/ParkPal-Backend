package at.technikum.parkpalbackend;

import at.technikum.parkpalbackend.dto.PictureDto;
import at.technikum.parkpalbackend.dto.VideoDto;
import at.technikum.parkpalbackend.dto.parkdtos.CreateParkDto;
import at.technikum.parkpalbackend.dto.parkdtos.ParkDto;
import at.technikum.parkpalbackend.dto.userdtos.CreateUserDto;
import at.technikum.parkpalbackend.dto.userdtos.DeleteUserDto;
import at.technikum.parkpalbackend.dto.userdtos.LoginUserDto;
import at.technikum.parkpalbackend.dto.userdtos.UserDto;
import at.technikum.parkpalbackend.model.*;
import at.technikum.parkpalbackend.model.enums.Role;
import at.technikum.parkpalbackend.model.enums.Gender;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class TestFixtures {

    public static Country austria = Country.builder().id("c07cd7cb-ce44-4709-a9b6-9d8d2d568263").name("Austria").iso2Code("AT").build();
    public static Country germany = Country.builder().name("Germany").iso2Code("AT").build();
    public static Address parkAddress = wien1010Address("mariahilfe Str.", 5);
    public static User adminUser = createUser("osama235", "sw@gmail.com", "Osama", "Mac", Role.ADMIN, Gender.MALE, "Mr.");

    public static DeleteUserDto adminDeleteUserDto = createDelteUserDto();
    public static LoginUserDto adminLoginUserDto = createLoginUserDto();


    public static CreateUserDto adminCreateUserDto = createCreateUserDTO("osama235", "sw@gmail.com", "Osama", "Mac", Role.ADMIN, Gender.MALE, "Mr.");
    public static UserDto adminUserDto = createUserDto("osama235", "sw@gmail.com", "Osama", "Mac", Role.ADMIN, Gender.MALE, "Mr.");
    public static User normalUser = createUser("r221", "raul@gmail.com", "Raul", "Gonzo", Role.USER, Gender.MALE, "Mr.");
    public static Park parkAwesome = createParkWithOutEvents("Awesome Park");

    public static Park parkWithEvents = createParkWithEvents("parkWithEvents");

    public static Park alternateParkWithEvents = createParkWithEvents("alternateParkWithEvents");

    public static Park parkLuca = createParkWithOutEvents("Park only For Lucas");

    public static ParkDto testParkDto = createTestParkDto("testParkDto");

    public static CreateParkDto testCreateParkDto = createCreateParkDto("testCreateParkDto");

   /* public static Media testMedia = createMedia();*/
   /* public static List<Media> mediaList = createMediaList();*/

    /*public static List<Event> eventList = createEventList();*/
    public static List<User> userList = createUserlist();
    // Events
    public static Event grilling = createEvent("grilling Biggest Steak Beef");
    public static Event pingPongGame = createEvent("pingPong Game with 4 players");
    public static Event chessMaster = createEvent("Chess Master only for the best players");
    public static Event pickNickWithYourFamily = createEvent("Pick Nick With Your Family");
    // EventTags
    public static EventTag familyEventTag = createEventTag("Family", grilling, pickNickWithYourFamily);
    public static EventTag gamesEventTag = createEventTag("Games", chessMaster, chessMaster);

    public static byte[] testFile = new byte[100];
    public static Picture testPicture = Picture.builder().id(UUID.randomUUID().toString())
            .user(normalUser)
            .uploadDate(LocalDateTime.now())
            .file(testFile).build();

    public static PictureDto testPictureDto = PictureDto.builder().id(UUID.randomUUID().toString())
            .userId(normalUser.getId())
            .uploadDate(LocalDateTime.now())
            .build();

    public static Picture alternateTestPicture = Picture.builder().id(UUID.randomUUID().toString())
            .user(normalUser)
            .uploadDate(LocalDateTime.now())
            .file(testFile).build();

    public static byte[] testVideoFile = new byte[100];
    public static Video testVideo = Video.builder().id(UUID.randomUUID().toString())
            .user(normalUser)
            .uploadDate(LocalDateTime.now())
            .file(testVideoFile).build();
    public static VideoDto testVideoDto = VideoDto.builder().id(UUID.randomUUID().toString())
            .userId(normalUser.getId())
            .uploadDate(LocalDateTime.now()).build();
    public static Video alternateTestVideo = Video.builder().id(UUID.randomUUID().toString())
            .user(normalUser)
            .uploadDate(LocalDateTime.now())
            .file(testVideoFile).build();

    private static List<EventTag> createEventTagListForAnEvent(Event event) {
        List<EventTag> eventTags = new ArrayList<>();
        eventTags.add(createEventTag("Family", event));
        eventTags.add(createEventTag("Football", event));
        return eventTags;
    }


    private static EventTag createEventTag(String eventTagName, Event ...events) {
        Set<Event> eventSet = new HashSet<>();
        for (Event event : events) {
            eventSet.add(event);
        }
        return EventTag.builder()
                .name(eventTagName)
                .events(eventSet)
                .build();
    }

    private static List<User> createUserlist() {
        List<User> joinedUsers = new ArrayList<>();
        joinedUsers.add(normalUser);
        joinedUsers.add(adminUser);
        return joinedUsers;
    }


    public static List<Picture> pictureList() {
        List<Picture> pictureList = new ArrayList<>();
        pictureList.add(testPicture);
        pictureList.add(alternateTestPicture);
        return pictureList;
    }
    public static List<Video> videoList() {
        List<Video> videoList = new ArrayList<>();
        videoList.add(testVideo);
        videoList.add(alternateTestVideo);
        return videoList;
    }


   /* private static List<Media> createMediaList() {
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(createMedia());
        mediaList.add(createMedia());
        return mediaList;
    }*/

    private static List<Event> createEventList() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(createEvent("EventTitle"));
        eventList.add(createEvent("EventTitle"));
        return eventList;
    }

    private static Event createEvent(String title) {
        Event event = Event.builder()
                .title(title)
                .description("Runaway Park")
                .startTS(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES))
                .endTS( LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES))
                .park(parkAwesome)
                .creator(adminUser)
                .joinedUsers(createUserlist())
                .build();
        List<EventTag> eventTags = createEventTagListForAnEvent(event);
        event.addEventTags(eventTags.toArray(EventTag[]::new));
        return event;
    }
    private static Park createParkWithOutEvents(String parkName) {
        return Park.builder()
                .name(parkName)
                .description("Park for Everybody")
                .address(parkAddress)
               /* .parkMedia(createMediaList())*/
                .build();
    }

    private static ParkDto createTestParkDto(String parkDtoName) {
        return ParkDto.builder()
                .name(parkDtoName)
                .description("ParkDTO Test")
                .address(parkAddress)
                .parkVideos(videoList())
                .parkPictures(pictureList())
                .parkEvents(createEventList())
                .build();
    }

    private static CreateParkDto createCreateParkDto (String createParkDto) {
        return CreateParkDto.builder()
                .name(createParkDto)
                .description("CreateParkDTO Test")
                .address(parkAddress)
                .build();
    }

    private static Park createParkWithEvents(String parkName) {
        return Park.builder()
                .name(parkName)
                .description("Park for Everybody")
                .address(parkAddress)
                .parkVideos(videoList())
                .parkPictures(pictureList())
                .parkEvents(createEventList())
                .build();
    }

  /* private static Media createMedia() {
        return Media.builder()
                .user(normalUser)
                .build();
    }*/

    private static UserDto createUserDto(String userName, String email, String firstName, String lastName, Role role, Gender gender, String salutation) {
        return UserDto.builder()
                .id(UUID.randomUUID().toString())
                .salutation(salutation)
                .gender(gender)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password("eyJhbGciOiJIUzI1NiIsInR5cCI32!")
                .countryId(austria.getId())
                .role(Role.ADMIN)
                .joinedEvents(createEventList())
                .build();
    }

    private static CreateUserDto createCreateUserDTO(String userName, String email, String firstName, String lastName, Role role, Gender gender, String salutation) {
        return CreateUserDto.builder()
                .id(UUID.randomUUID().toString())
                .salutation(salutation)
                .gender(gender)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password("eyJhbGciOiJIUzI1NiIsInR5cCI32!")
                .countryId(austria.getId())
                .role(Role.ADMIN)
                .build();
    }

    private static DeleteUserDto createDelteUserDto() {
        return DeleteUserDto.builder()
                .userId(UUID.randomUUID().toString())
                .build();

    }

    private static LoginUserDto createLoginUserDto() {
        return LoginUserDto.builder()
                .email(UUID.randomUUID().toString())
                .build();

    }

    private static User createUser(String userName, String email, String firstName, String lastName, Role role, Gender gender, String salutation) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .salutation(salutation)
                .gender(gender)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password("eyJhbGciOiJIUzI1NiIsInR5cCI32!")
                .country(austria)
                .role(Role.ADMIN)
                .joinedEvents(createEventList())
                .build();
    }

    public static Address wien1010Address(String streetName, Integer number) {
        return Address.builder()
                .streetNumber(streetName)
                .zipCode("1010")
                .city("Wien")
                .country(austria)
                .build();
    }
}
