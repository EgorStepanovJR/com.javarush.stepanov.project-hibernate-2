package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import service.RatingConverter;
import service.YearInFilmConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Entity
@Table(name = "film", schema = "movie")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    Short id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description", columnDefinition = "text")
    @Type(type = "text")
    String description;

    @Column(name = "release_year", columnDefinition = "year")
    @Convert(converter = YearInFilmConverter.class)
    Year releaseYear;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    Language originalLanguage;

    @Column(name = "rental_duration", nullable = false)
    Byte rentalDuration;

    @Column(name = "rental_rate", nullable = false)
    BigDecimal rentalRate;

    @Column(name = "length")
    Short length;

    @Column(name = "replacement_cost", nullable = false)
    BigDecimal replacementCost;


    @Column(name = "rating", columnDefinition = "enum('G', 'PG', 'PG-13', 'R', 'NC-17')")
    @Convert(converter = RatingConverter.class)
    Rating rating;

    @Column(name = "special_features", columnDefinition = "set('Trailers', 'Commentaries', 'Deleted Scenes', 'Behind the Scenes')")
    String specialFeatures;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    LocalDateTime lastUpdate;

    @ManyToMany
    @JoinTable(name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "actor_id"))
    @ToString.Exclude
    Set<Actor> actors;

    @ManyToMany
    @JoinTable(name = "film_category",
            joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "category_id"))
    @ToString.Exclude
    Set<Category> categories;

    @OneToMany(mappedBy = "film")
    @ToString.Exclude
    Set<Inventory> inventories;

    public void setSpecialFeatures(Set<Feature> features) {
        if (isNull(features)) specialFeatures = null;
        else specialFeatures = features.stream().map(Feature::getValue).collect(Collectors.joining(","));
    }

    public Set<Feature> getSpecialFeatures() {
        if (isNull(specialFeatures) || specialFeatures.isEmpty()) return null;

        Set<Feature> featureSet = new HashSet<>();

        String[] specialFeaturesArray = specialFeatures.split(",");
        for (String str : specialFeaturesArray) {
            featureSet.forEach(feature -> featureSet.add(Feature.getFeatureValue(str)));
        }
        featureSet.remove(null);
        return featureSet;
    }
}
