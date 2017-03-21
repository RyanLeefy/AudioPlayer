package com.example.administrator.audioplayer.Iview;

import com.example.administrator.audioplayer.adapter.SongListAdapter;
import com.example.administrator.audioplayer.bean.CollectionInfo;

import com.example.administrator.audioplayer.bean.MusicFragmentExpandItem;

import java.util.List;

/**
 * MusicFragment
 * Created by on 2017/2/15.
 */

public interface IMusicView {
    void setAdapter( SongListAdapter adapter,
                            List allItems,
                            List<CollectionInfo> create_songCollectionItems,
                            List<CollectionInfo> collect_songCollectionItems,
                            List<MusicFragmentExpandItem> expandItemList);
}
