package utils;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.model.entity.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.TAG_NAME1;
import static utils.LibraryTestConstants.TAG_NAME2;
import static utils.LibraryTestConstants.TAG_NAME3;

public class TestTagFactory {

    public static Tag createTag1() {
        Tag tag = new Tag();
        tag.setId(ID1);
        tag.setName(TAG_NAME1);
        return tag;
    }

    public static Tag createTag2() {
        Tag tag = new Tag();
        tag.setId(ID2);
        tag.setName(TAG_NAME2);
        return tag;
    }

    public static Tag createTag3() {
        Tag tag = new Tag();
        tag.setId(ID3);
        tag.setName(TAG_NAME3);
        return tag;
    }

    public static List<Tag> createTagList() {
        List<Tag> list = new ArrayList<>();
        list.add(createTag1());
        list.add(createTag2());
        return list;
    }

    public static Set<Tag> createTagSet1() {
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(createTag1());
        tagSet.add(createTag2());
        return tagSet;
    }

    public static Set<Tag> createTagSet2() {
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(createTag1());
        return tagSet;
    }

    public static TagRequestDto createTagRequestDto1() {
        return new TagRequestDto(TAG_NAME1);
    }

    public static TagRequestDto createTagRequestDto2() {
        return new TagRequestDto(TAG_NAME2);
    }

    public static TagRequestDto createTagRequestDto3() {
        return new TagRequestDto(TAG_NAME3);
    }

    public static Set<TagRequestDto> createTagsRequestDto() {
        Set<TagRequestDto> tagsRequestDto = new HashSet<>();
        tagsRequestDto.add(createTagRequestDto1());
        tagsRequestDto.add(createTagRequestDto2());
        return tagsRequestDto;
    }

    public static Set<TagRequestDto> createTagsRequestDto2() {
        Set<TagRequestDto> tagsRequestDto = new HashSet<>();
        tagsRequestDto.add(createTagRequestDto1());
        return tagsRequestDto;
    }
}


