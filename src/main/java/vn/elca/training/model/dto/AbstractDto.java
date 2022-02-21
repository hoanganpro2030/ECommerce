package vn.elca.training.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AbstractDto {
    protected Long id;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private Integer version;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof AbstractDto) {
            final AbstractDto other = (AbstractDto) obj;
            return getId().equals(other.getId());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
        // The above mechanism obey the rule: if 2 objects are equal, their hashcode must be same.
    }
}
